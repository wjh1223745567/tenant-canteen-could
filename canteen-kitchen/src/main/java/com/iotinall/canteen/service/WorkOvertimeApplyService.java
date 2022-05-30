package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.AttendanceGroupTypeEnum;
import com.iotinall.canteen.constant.DayTypeEnum;
import com.iotinall.canteen.constant.WorkOverTypeEnum;
import com.iotinall.canteen.constant.WorkOvertimeApplyStateEnum;
import com.iotinall.canteen.controller.WorkOvertimeApplyController;
import com.iotinall.canteen.dto.holiday.FeignHolidayDTO;
import com.iotinall.canteen.dto.overtime.*;
import com.iotinall.canteen.entity.*;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkOvertimeApplyService {
    @Resource
    private WorkOvertimeApplyRepository applyRepository;
    @Resource
    private FeignHolidayService feignHolidayService;
    @Resource
    private AttendanceRecordService attendanceRecordService;
    @Resource
    private WorkOvertimeRecordRepository recordRepository;
    @Resource
    private WorkOvertimeRecordService overtimeRecordService;

    /**
     * 分页查询
     *
     * @return
     */
    public PageDTO<WorkOvertimeApplyDto> findPage(WorkOvertimeApplyCondition condition, Pageable pageable) {
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Criterion.like("employee.name", condition.getName()));

        if (SecurityUtils.getCurrentUser().getPermissions().stream().noneMatch(item -> ArrayUtils.contains(new String[]{"ADMIN", WorkOvertimeApplyController.modelKey + "_ALL", WorkOvertimeApplyController.modelKey + "_APPLY"}, item))) {
            criterions.add(Criterion.eq("createId", SecurityUtils.getUserId()));
        }
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createTime"));
        criterions.add(Criterion.gte("startTime", condition.getStartTime()));

        if (condition.getStartTime() != null && condition.getEndTime() != null) {
            criterions.add(Criterion.lte("endTime", condition.getStartTime().isEqual(condition.getEndTime()) ? condition.getEndTime().plusDays(1) : condition.getEndTime()));
        }

        criterions.add(Criterion.eq("state", condition.getState()));

        Specification<WorkOvertimeApply> specification = SpecificationBuilder.builder()
                .where(criterions.toArray(new Criterion[0]))
                .build();
        Page<WorkOvertimeApplyDto> page = this.applyRepository.findAll(specification, pageable1).map(item -> {
            WorkOvertimeApplyDto applyDto = new WorkOvertimeApplyDto()
                    .setId(item.getId())
                    .setCreateTime(item.getCreateTime())
                    //.setEmployeeName(item.getEmployee() != null ? item.getEmployee().getName() : null)
                    .setEndTime(item.getEndTime())
                    .setAuditOpinion(item.getAuditOpinion())
                    //.setPosition(item.getEmployee() != null ? item.getEmployee().getRole() : null)
                    .setStartTime(item.getStartTime())
                    //.setUsed(Objects.equals(SecurityUtils.getUserId(), item.getEmployee() != null ? item.getEmployee().getId() : null))
                    .setState(item.getState())
                    .setStateName(WorkOvertimeApplyStateEnum.findByCode(item.getState()).getName())
                    .setUpdateTime(item.getUpdateTime());
            return applyDto;
        });
        return PageUtil.toPageDTO(page);
    }

    /**
     * 添加申请
     *
     * @param req
     */
    public void add(WorkOvertimeApplyAddReq req) {
        //验证是否能加班
//        OrgEmployee employee = this.validApply(req);
//
//        WorkOvertimeApply apply = this.applyRepository.findApplyAll(employee, req.getStartTime().toLocalDate());
//        if (apply == null) {
//            apply = new WorkOvertimeApply();
//        } else if (Objects.equals(apply.getState(), WorkOvertimeApplyStateEnum.SUCCESS.getCode())) {
//            this.removeRecord(apply.getId());
//        }
//        apply.setEmployee(employee)
//                .setStartTime(req.getStartTime())
//                .setEndTime(req.getEndTime())
//                .setThisDay(req.getStartTime().toLocalDate())
//                .setCreateId(SecurityUtils.getUserId())
//                .setState(WorkOvertimeApplyStateEnum.PENDING.getCode());
//        this.applyRepository.save(apply);
//
//        //找到具有审核加班权限的用户，发送消息
//        List<OrgEmployee> orgEmployees = this.employeeRepository.findAll();
//        if (CollectionUtils.isEmpty(orgEmployees)) {
//            throw new BizException("", "未找到用户表");
//        }
//        for (OrgEmployee orgEmployee : orgEmployees) {
//            Set<SysRole> sysRoleSet = orgEmployee.getRoles();
//            if (!CollectionUtils.isEmpty(sysRoleSet)) {
//                for (SysRole sysRole : sysRoleSet) {
//                    Set<SysPermission> sysPermissionSet = sysRole.getPermissions();
//                    if (!CollectionUtils.isEmpty(sysPermissionSet)) {
//                        for (SysPermission sysPermission : sysPermissionSet) {
//                            if (Objects.equals(sysPermission.getPermission(), "WORK_OVERTIME_APPLY_APPLY")) {
////                                log.info("审核人:" + orgEmployee.getName());
////                                log.info("申请人：" + employee.getName());
////                                log.info("所属部门：" + employee.getRole());
////                                log.info("审核内容：" + "申请加班");
////                                log.info("审核状态：" + WorkOvertimeApplyStateEnum.PENDING.getName());
////                                this.wxMessageSendService.sendKitchenManagementReviewerMessage(orgEmployee.getOpenid(), employee.getName(), employee.getRole(), "申请加班", WorkOvertimeApplyStateEnum.PENDING.getName(), MiniProgramPageEnum.KITCHEN_MANAGEMENT_APPLICANT_MENU.getValue());
//                            }
//                        }
//                    }
//                }
//            }
//        }

    }

    /**
     * 按审核时间算加班时间时直接添加加班记录
     *
     * @param empId
     * @param startTime
     * @param endTime
     */
    private void addRecord(Long empId, LocalDateTime startTime, LocalDateTime endTime, Long id) {
        AttendanceGroup attendanceGroup = attendanceRecordService.getKitchenEmployeeAttendanceGroup(empId);
        if (attendanceGroup != null) {
            try {
                WorkRecordDto recordDto = this.findDayType(attendanceGroup, startTime);
                if (Objects.equals(recordDto.getOverTypeEnum(), WorkOverTypeEnum.PROCESSING_TIME)) {
                    WorkOvertimeRecord overtimeRecord = this.recordRepository.findLastRecord(empId, attendanceGroup);
                    if (overtimeRecord == null) {
                        overtimeRecord = new WorkOvertimeRecord();
                    }
                    overtimeRecord.setStartTime(startTime)
                            .setEndTime(endTime)
                            //.setEmployee(employee)
                            .setApplyId(id)
                            .setCreateId(SecurityUtils.getUserId());
                    //.setGroup(attendanceGroup);
                    this.overtimeRecordService.calculateTime(overtimeRecord);
                    this.recordRepository.save(overtimeRecord);
                }
            } catch (BizException e) {
            }
        }

    }

    /**
     * 移除加班记录
     */
    private void removeRecord(Long applyId) {
        if (applyId == null) {
            return;
        }
        List<WorkOvertimeRecord> overtimeRecords = this.recordRepository.findByApplyId(applyId);
        if (!overtimeRecords.isEmpty()) {
            this.recordRepository.deleteAll(overtimeRecords);
        }
    }

    /**
     * 编辑
     *
     * @param req
     */
    public void edit(WorkOvertimeApplyAddReq req) {
        if (req.getId() == null) {
            throw new BizException("", "ID不能为空");
        }
        WorkOvertimeApply apply = this.applyRepository.findById(req.getId()).orElseThrow(() -> new BizException("", "未找到当前数据"));
        //验证是否能加班
//        OrgEmployee employee = this.validApply(req);
//        apply.setEmployee(employee)
//                .setStartTime(req.getStartTime())
//                .setEndTime(req.getEndTime())
//                .setThisDay(req.getStartTime().toLocalDate())
//                .setState(WorkOvertimeApplyStateEnum.PENDING.getCode());
//        this.applyRepository.save(apply);
    }

    /**
     * 取消申请
     *
     * @param id
     */
    public void cancel(Long id) {
        WorkOvertimeApply overtimeApply = this.applyRepository.findById(id).orElseThrow(() -> new BizException("", "未找到要取消的数据"));
        if (overtimeApply.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BizException("", "无法取消过去提交的申请");
        }
        this.applyRepository.delete(overtimeApply);
        this.removeRecord(overtimeApply.getId());
    }

    /**
     * 审核
     */
    public void apply(WorkOvertimeApplyReq req) {
        Integer count = this.applyRepository.applyData(req.getState(), req.getId(), req.getAuditOpinion());
        if (count < 1) {
            throw new BizException("", "审核失败");
        }
        if (req.getState() == 1) {
            WorkOvertimeApply apply = this.applyRepository.findById(req.getId()).orElseThrow(() -> new BizException("", "未找到数据"));
            if (apply.getEndTime().isBefore(LocalDateTime.now())) {
                throw new BizException("", "数据已过期");
            }
            //this.addRecord(apply.getEmployee(), apply.getStartTime(), apply.getEndTime(), apply.getId());
//            log.info("申请人：" + apply.getEmployee().getName());
//            log.info("所属部门：" + apply.getEmployee().getRole());
//            log.info("审核内容：" + "申请加班");
//            log.info("审核状态：" + WorkOvertimeApplyStateEnum.findByCode(apply.getState()).getName());
//            this.wxMessageSendService.sendkitchenManagementApplicantMessage(apply.getEmployee().getOpenid(), apply.getEmployee().getName(), apply.getEmployee().getRole(), "申请加班", WorkOvertimeApplyStateEnum.findByCode(apply.getState()).getName(), MiniProgramPageEnum.KITCHEN_MANAGEMENT_APPLICANT_MENU.getValue());
        }
    }

    /**
     * 验证是否符合加班配置情况
     */
//    private OrgEmployee validApply(WorkOvertimeApplyAddReq req) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        if (!Objects.equals(req.getStartTime().format(formatter), req.getEndTime().format(formatter))) {
//            throw new BizException("", "申请时长不能超过一天");
//        }
//
//        if (req.getStartTime().isAfter(req.getEndTime())) {
//            throw new BizException("", "开始时间要小于结束时间");
//        }
//
//        if (req.getStartTime().isBefore(LocalDateTime.now())) {
//            throw new BizException("", "开始时间不能小于当前时间");
//        }
//
//        AttendanceGroupMember attendanceGroupMember = groupMemberRepository.findByEmpId(SecurityUtils.getUserId());
//        if (attendanceGroupMember == null) {
//            throw new BizException("", "当前用户未被拉入班次组");
//        }
//        AttendanceGroup attendanceGroup = this.groupRepository.findById(attendanceGroupMember.getGroupId()).orElseThrow(() -> new BizException("", "未找到班次组"));
//
//        if (attendanceGroup.getOvertimeConfig() == null) {
//            throw new BizException("", "考勤组未配置加班规则，无法申请");
//        }
//
//        WorkOvertimeConfig config = attendanceGroup.getOvertimeConfig();
//        if (!config.getOnHolidaysEnable() && !config.getOnRestDaysEnable() && !config.getWorkOvertimeEnable()) {
//            throw new BizException("", "考勤组不允许加班，无法申请");
//        }
//
//        if (config.getWorkOvertimeEnable() && Objects.equals(config.getWorkOverType(), WorkOverTypeEnum.NO_APPROVAL.getCode())
//                && config.getOnHolidaysEnable() && Objects.equals(config.getOnHolidaysType(), WorkOverTypeEnum.NO_APPROVAL.getCode())
//                && config.getOnRestDaysEnable() && Objects.equals(config.getOnRestDaysType(), WorkOverTypeEnum.NO_APPROVAL.getCode())
//        ) {
//            throw new BizException("", "考勤组设置为按打卡时长计算加班时间，无需申请");
//        }
//
//        //判断是否是节假日加班
//        LocalDate nowDay = req.getStartTime().toLocalDate();
//        this.holidayDateRepository.findById(nowDay).ifPresent(item -> {
//            this.validHolidays(config);
//        });
//
//        Integer week = req.getStartTime().getDayOfWeek().getValue();
//        switch (AttendanceGroupTypeEnum.findByCode(attendanceGroup.getType())) {
//            case FIXED: {
//                //固定班制验证
//                Set<AttendanceGroupShift> groupShifts = attendanceGroup.getShiftList();
//                AttendanceGroupShift groupShift = groupShifts.stream().filter(item -> Objects.equals(week, item.getWeekday())).findFirst().orElseThrow(() -> new BizException("", "未找到周" + week + "班次信息，无法申请"));
//                AttendanceShift attendanceShift = groupShift.getShifts();
//                if (attendanceShift != null) {
//                    //当前上班
//                    this.validWorkDay(config, attendanceShift, req.getStartTime());
//                } else {
//                    //休息日
//                    this.validPlayDay(config);
//                }
//                break;
//            }
//            case SHIFT_SYSTEM: {
//                //排班制验证
//                Set<AttendanceGroupShiftSystem> shiftSystems = attendanceGroup.getShiftSystems();
//                AttendanceGroupShiftSystem shiftSystem = shiftSystems.stream().filter(item -> item.getEmployee() != null && Objects.equals(item.getEmployee().getId(), SecurityUtils.getUserId())).findFirst().orElseThrow(() -> new BizException("", "未找到排班信息"));
//
//                AttendanceGroupShiftSystemDate shiftSystemDate = shiftSystem.getShiftSystemDateList().stream().filter(item -> Objects.equals(item.getDate(), nowDay)).findFirst().orElseThrow(() -> new BizException("", "未找到排班信息"));
//                if (shiftSystemDate.getShift() == null) {
//                    //休息日
//                    this.validPlayDay(config);
//                } else {
//                    //工作日
//                    this.validWorkDay(config, shiftSystemDate.getShift(), req.getStartTime());
//                }
//                break;
//            }
//        }
//        return employee;
//    }

    /**
     * 查询天类型
     *
     * @return
     */
    public WorkRecordDto findDayType(AttendanceGroup attendanceGroup, LocalDateTime dateTime) {
        WorkRecordDto recordDto = new WorkRecordDto();
        WorkOvertimeConfig config = attendanceGroup.getOvertimeConfig();
        if (!config.getOnHolidaysEnable() && !config.getOnRestDaysEnable() && !config.getWorkOvertimeEnable()) {
            throw new BizException("", "考勤组不允许加班，无法申请");
        }

        //判断是否是节假日加班
        LocalDate nowDay = dateTime.toLocalDate();
        try {
            FeignHolidayDTO feignHolidayDTO = this.feignHolidayService.getByDate(nowDay);
            if (null != feignHolidayDTO && feignHolidayDTO.getHoliday()) {
                recordDto.setTypeEnum(DayTypeEnum.HOLIDAYS);
                recordDto.setOverTypeEnum(WorkOverTypeEnum.findByCode(config.getOnHolidaysType()));
                this.validHolidays(config);
            }
        } catch (BizException e) {
            if (Objects.equals(e.getCode(), "1")) {
            
            } else {
                throw e;
            }
        }
        if (recordDto.getTypeEnum() != null) {
            return recordDto;
        }

        Integer week = dateTime.getDayOfWeek().getValue();
        switch (AttendanceGroupTypeEnum.findByCode(attendanceGroup.getType())) {
            case FIXED: {
                //固定班制验证
                Set<AttendanceGroupShift> groupShifts = attendanceGroup.getShiftList();
                AttendanceGroupShift groupShift = groupShifts.stream().filter(item -> Objects.equals(week, item.getWeekday())).findFirst().orElseThrow(() -> new BizException("", "未找到周" + week + "班次信息，无法申请"));
                AttendanceShift attendanceShift = groupShift.getShifts();
                if (attendanceShift != null) {
                    //当前上班
                    recordDto.setShift(attendanceShift);
                    recordDto.setTypeEnum(DayTypeEnum.WORKING_DAY);
                    try {
                        recordDto.setOverTypeEnum(WorkOverTypeEnum.findByCode(config.getWorkOverType()));
                        this.validWorkDay(config, attendanceShift, dateTime);
                    } catch (BizException e) {
                        if (Objects.equals(e.getCode(), "1")) {
                        } else {
                            throw e;
                        }
                    }
                } else {
                    //休息日
                    recordDto.setTypeEnum(DayTypeEnum.PLAY_DAY);
                    try {
                        recordDto.setOverTypeEnum(WorkOverTypeEnum.findByCode(config.getOnRestDaysType()));
                        this.validPlayDay(config);
                    } catch (BizException e) {
                        if (Objects.equals(e.getCode(), "1")) {
                        } else {
                            throw e;
                        }
                    }
                }
                break;
            }
            case SHIFT_SYSTEM: {
                //排班制验证
                Set<AttendanceGroupShiftSystem> shiftSystems = attendanceGroup.getShiftSystems();
                //AttendanceGroupShiftSystem shiftSystem = shiftSystems.stream().filter(item -> item.getEmployee() != null && Objects.equals(item.getEmployee().getId(), SecurityUtils.getUserId())).findFirst().orElseThrow(() -> new BizException("", "未找到排班信息"));

//                AttendanceGroupShiftSystemDate shiftSystemDate = shiftSystem.getShiftSystemDateList().stream().filter(item -> Objects.equals(item.getDate(), nowDay)).findFirst().orElseThrow(() -> new BizException("", "未找到排班信息"));
//                if (shiftSystemDate.getShift() == null) {
//                    //休息日
//                    recordDto.setTypeEnum(DayTypeEnum.PLAY_DAY);
//                    try {
//                        recordDto.setOverTypeEnum(WorkOverTypeEnum.findByCode(config.getOnRestDaysType()));
//                        this.validPlayDay(config);
//                    } catch (BizException e) {
//                        if (Objects.equals(e.getCode(), "1")) {
//
//                        } else {
//                            throw e;
//                        }
//                    }
//                } else {
//                    //工作日
//                    recordDto.setShift(shiftSystemDate.getShift());
//                    recordDto.setTypeEnum(DayTypeEnum.WORKING_DAY);
//                    try {
//                        recordDto.setOverTypeEnum(WorkOverTypeEnum.findByCode(config.getWorkOverType()));
//                        this.validWorkDay(config, shiftSystemDate.getShift(), dateTime);
//                    } catch (BizException e) {
//                        if (Objects.equals(e.getCode(), "1")) {
//
//                        } else {
//                            throw e;
//                        }
//                    }
//                }
                break;
            }
        }
        return recordDto;
    }

    /**
     * 验证节假日
     */
    private void validHolidays(WorkOvertimeConfig config) {
        if (!config.getOnHolidaysEnable()) {
            throw new BizException("", "节假日不允许加班");
        } else {
            if (Objects.equals(config.getOnHolidaysType(), WorkOverTypeEnum.NO_APPROVAL.getCode())) {
                throw new BizException("1", "考勤组设置为按打卡时长计算加班时间，无需申请");
            }
        }
    }

    /**
     * 验证工作时间
     *
     * @param config
     * @param attendanceShift
     * @param startTime
     */
    private void validWorkDay(WorkOvertimeConfig config, AttendanceShift attendanceShift, LocalDateTime startTime) {
        if (!config.getWorkOvertimeEnable()) {
            throw new BizException("", "工作日不允许加班");
        } else {
            if (Objects.equals(config.getWorkOverType(), WorkOverTypeEnum.NO_APPROVAL.getCode())) {
                throw new BizException("1", "考勤组设置为按打卡时长计算加班时间，无需申请");
            }
        }

        List<AttendanceShiftTime> shiftTimes = attendanceShift.getShiftTimes();
        if (CollectionUtils.isEmpty(shiftTimes)) {
            throw new BizException("", "未找到周班次时间信息，无法申请");
        }

        AttendanceShiftTime shiftTime = shiftTimes.get(shiftTimes.size() - 1);
        if (shiftTime.getOffWorkTime().isAfter(startTime.toLocalTime())) {
            throw new BizException("", "请输入非上班时间段范围");
        }
    }

    /**
     * 验证休息日
     */
    private void validPlayDay(WorkOvertimeConfig config) {
        if (!config.getOnRestDaysEnable()) {
            throw new BizException("", "休息日不允许加班");
        } else {
            if (Objects.equals(config.getOnRestDaysType(), WorkOverTypeEnum.NO_APPROVAL.getCode())) {
                throw new BizException("1", "考勤组设置为按打卡时长计算加班时间，无需申请");
            }
        }
    }
}
