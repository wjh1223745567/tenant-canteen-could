package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.WorkOverTypeEnum;
import com.iotinall.canteen.controller.WorkOvertimeRecordController;
import com.iotinall.canteen.dto.overtime.WorkOvertimeRecordCondition;
import com.iotinall.canteen.dto.overtime.WorkOvertimeRecordDto;
import com.iotinall.canteen.dto.overtime.WorkRecordDto;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.WorkOvertimeApplyRepository;
import com.iotinall.canteen.repository.WorkOvertimeRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.iotinall.canteen.constant.DayTypeEnum.*;

/**
 * 加班记录
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkOvertimeRecordService {
    @Resource
    private WorkOvertimeRecordRepository recordRepository;
    @Resource
    private WorkOvertimeApplyRepository applyRepository;
    @Resource
    private WorkOvertimeApplyService applyService;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 分页查询
     *
     * @param condition
     * @param pageable
     * @return
     */
    public PageDTO<WorkOvertimeRecordDto> findPage(WorkOvertimeRecordCondition condition, Pageable pageable) {
        List<Criterion> criterionList = new ArrayList<>();
        criterionList.add(Criterion.like("employee.name", condition.getName()));
        if (SecurityUtils.getCurrentUser()
                .getPermissions()
                .stream()
                .noneMatch(item -> ArrayUtils.contains(new String[]{"ADMIN", WorkOvertimeRecordController.modelKey + "_ALL", WorkOvertimeRecordController.modelKey + "_ADE"}, item))) {
            criterionList.add(Criterion.eq("createId", SecurityUtils.getUserId()));
        }
        criterionList.add(Criterion.gte("startTime", condition.getStartTime()));
        if (condition.getEndTime() != null) {
            criterionList.add(Criterion.lte("endTime", condition.getEndTime().plusDays(1)));
        }

        Specification<WorkOvertimeRecord> specification = SpecificationBuilder.builder()
                .where(criterionList.toArray(new Criterion[0]))
                .build();
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createTime"));
        Page<WorkOvertimeRecordDto> page = this.recordRepository.findAll(specification, pageable1).map(item -> {
            WorkOvertimeRecordDto recordDto = new WorkOvertimeRecordDto()
                    .setId(item.getId())
                    .setCreateTime(item.getCreateTime())
                    .setEndTime(item.getEndTime())
                    .setGroupName(item.getGroup() != null ? item.getGroup().getName() : null)
                    .setStartTime(item.getStartTime())
                    .setUpdateTime(item.getUpdateTime());

            FeignEmployeeDTO employeeDTO = feignEmployeeService.findById(item.getEmpId());
            if (null != employeeDTO) {
                recordDto.setEmployeeName(employeeDTO.getName());
                recordDto.setPosition(employeeDTO.getPosition());
            }
            return recordDto;
        });
        return PageUtil.toPageDTO(page);
    }

    /**
     * 生成加班记录
     *
     * @param time
     */
    public Boolean createRecord(LocalDateTime time, AttendanceGroup group, FeignSimEmployeeDto employee) {
        if (group.getOvertimeConfig() == null) {
            return false;
        }
        WorkOvertimeConfig overtimeConfig = group.getOvertimeConfig();
        WorkRecordDto recordDto = this.applyService.findDayType(group, time);
        if (Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.PROCESSING_TIME)) {
            return false;
        }

        switch (recordDto.getTypeEnum()) {
            case HOLIDAYS:
            case PLAY_DAY: {
                WorkOvertimeRecord overtimeRecord = this.recordRepository.findLastRecord(employee.getId(), group);
                List<WorkOvertimeApply> applies = new ArrayList<>(0);
                //按审批时长计算
                if (Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.CHECK_IN_TIME)) {
                    applies = this.applyRepository.findApply(employee.getId(), time.toLocalDate());
                    if (CollectionUtils.isEmpty(applies)) {
                        throw new BizException("", "当前未申请加班无法生成加班记录");
                    }
                }

                if (overtimeRecord != null) {
                    //距离上次打卡时间相差多久
                    Long subMinutes = Math.abs(Duration.between(overtimeRecord.getUpdateTime(), time).toMinutes());
                    if (subMinutes > 1) {
                        //两次打卡时间相差一分钟时更新结束打卡时间
                        if (applies.isEmpty() && Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.NO_APPROVAL)) {
                            overtimeRecord.setEndTime(time);
                        } else if (!applies.isEmpty() && Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.CHECK_IN_TIME)) {
                            //按申请时间最短记录加班时间
                            WorkOvertimeApply apply = applies.get(applies.size() - 1);
                            overtimeRecord.setEndTime(time.isBefore(apply.getEndTime()) ? time : apply.getEndTime());
                        }
                    }
                } else {
                    overtimeRecord = new WorkOvertimeRecord()
                            .setGroup(group)
                            .setEmpId(employee.getId());
                    overtimeRecord.setCreateId(employee.getId());

                    if (applies.isEmpty()) {
                        overtimeRecord.setStartTime(time);
                    } else {
                        WorkOvertimeApply apply = applies.get(applies.size() - 1);
                        overtimeRecord.setStartTime(time.isBefore(apply.getStartTime()) ? apply.getStartTime() : time);
                    }
                }
                Integer minTime = Objects.equals(recordDto.getTypeEnum(), HOLIDAYS) ? overtimeConfig.getOnHolidaysLess() : overtimeConfig.getOnRestDaysLess();
                if (overtimeRecord.getId() != null && overtimeRecord.getStartTime() != null && overtimeRecord.getEndTime() != null &&
                        (overtimeRecord.getStartTime().isAfter(overtimeRecord.getEndTime())
                                || Math.abs(Duration.between(overtimeRecord.getStartTime(), overtimeRecord.getEndTime()).toMinutes()) < minTime)) {
                    this.recordRepository.delete(overtimeRecord);
                } else {
                    this.calculateTime(overtimeRecord);
                    this.recordRepository.save(overtimeRecord);
                    return true;
                }
                break;
            }
            case WORKING_DAY: {
                if (recordDto.getShift() != null) {
                    AttendanceShift attendanceShift = recordDto.getShift();
                    AttendanceShiftTime shiftTime = attendanceShift.getShiftTimes().isEmpty() ? null : attendanceShift.getShiftTimes().get(attendanceShift.getShiftTimes().size() - 1);

                    //按打卡时长计算
                    if (Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.NO_APPROVAL)) {
                        //大于设置时间才算入加班
                        if (shiftTime != null && shiftTime.getOffWorkTime().isBefore(time.toLocalTime())
                                && Math.abs(Duration.between(shiftTime.getOffWorkTime().plusMinutes(overtimeConfig.getWorkOverStart()), time.toLocalTime()).toMinutes()) > overtimeConfig.getWorkOverLess()) {
                            List<WorkOvertimeRecord> nowDayRecord = this.recordRepository.findDayRecord(employee.getId(), group, LocalDateTime.of(time.toLocalDate(), LocalTime.MIN), LocalDateTime.of(time.toLocalDate(), LocalTime.MAX));
                            WorkOvertimeRecord overtimeRecord = new WorkOvertimeRecord();
                            if (!nowDayRecord.isEmpty()) {
                                overtimeRecord = nowDayRecord.get(nowDayRecord.size() - 1);
                            }
                            overtimeRecord.setEmpId(employee.getId())
                                    .setGroup(group)
                                    .setStartTime(LocalDateTime.of(LocalDate.now(), shiftTime.getOffWorkTime().plusMinutes(overtimeConfig.getWorkOverStart())))
                                    .setEndTime(time);
                            overtimeRecord.setCreateId(employee.getId());
                            this.calculateTime(overtimeRecord);
                            this.recordRepository.save(overtimeRecord);
                            return true;
                        }
                    } else if (Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.CHECK_IN_TIME)) {
                        List<WorkOvertimeApply> applies = new ArrayList<>(0);
                        //按审批时长计算
                        if (Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.CHECK_IN_TIME)) {
                            applies = this.applyRepository.findApply(employee.getId(), time.toLocalDate());
                            if (CollectionUtils.isEmpty(applies)) {
                                throw new BizException("", "当前未申请加班无法生成加班记录");
                            }
                        }
                        if (!applies.isEmpty()) {
                            WorkOvertimeApply apply = applies.get(applies.size() - 1);
                            LocalTime endTime = apply.getEndTime().isBefore(time) ? apply.getEndTime().toLocalTime() : time.toLocalTime();
                            if (shiftTime != null && shiftTime.getOffWorkTime().isBefore(time.toLocalTime())
                                    && Math.abs(Duration.between(apply.getStartTime().plusMinutes(overtimeConfig.getWorkOverStart()).toLocalTime(), endTime).toMinutes()) > overtimeConfig.getWorkOverLess()) {
                                List<WorkOvertimeRecord> nowDayRecord = this.recordRepository.findDayRecord(employee.getId(), group, LocalDateTime.of(time.toLocalDate(), LocalTime.MIN), LocalDateTime.of(time.toLocalDate(), LocalTime.MAX));
                                WorkOvertimeRecord overtimeRecord = new WorkOvertimeRecord();
                                if (!nowDayRecord.isEmpty()) {
                                    overtimeRecord = nowDayRecord.get(nowDayRecord.size() - 1);
                                }
                                overtimeRecord
                                        .setEmpId(employee.getId())
                                        .setGroup(group)
                                        .setStartTime(LocalDateTime.of(LocalDate.now(), shiftTime.getOffWorkTime().plusMinutes(overtimeConfig.getWorkOverStart())))
                                        .setEndTime(time);
                                overtimeRecord.setCreateId(employee.getId());
                                this.calculateTime(overtimeRecord);
                                this.recordRepository.save(overtimeRecord);
                                return true;
                            }
                        }
                    }

                }
                break;
            }
            default: {

            }
        }

        return false;
    }

    /**
     * 计算加班时长
     *
     * @param overtimeRecord
     */
    public void calculateTime(WorkOvertimeRecord overtimeRecord) {
        if (overtimeRecord.getStartTime() != null && overtimeRecord.getEndTime() != null) {
            overtimeRecord.setOvertime(Duration.between(overtimeRecord.getStartTime(), overtimeRecord.getEndTime()).toMinutes());
        }
    }

    /**
     * 加班时长
     *
     * @param empId
     * @param startTime
     * @param endTime
     * @return
     */
    public Long overtime(Long empId, LocalDateTime startTime, LocalDateTime endTime) {
        List<WorkOvertimeRecord> times = this.recordRepository.findAllTime(empId, startTime, endTime);

        Long minutes = times.stream().mapToLong(item -> Math.abs(Duration.between(item.getStartTime(), item.getEndTime()).toMinutes())).sum();
        return minutes / 60;
    }

}
