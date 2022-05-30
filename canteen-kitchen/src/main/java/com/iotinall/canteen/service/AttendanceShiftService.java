package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.attendanceshift.*;
import com.iotinall.canteen.entity.AttendanceShift;
import com.iotinall.canteen.entity.AttendanceShiftTime;
import com.iotinall.canteen.repository.AttendanceShiftRepository;
import com.iotinall.canteen.repository.AttendanceShiftTimeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * attendance_shift ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-13 19:54:53
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AttendanceShiftService {
    @Resource
    private AttendanceShiftRepository attendanceShiftRepository;
    @Resource
    private AttendanceShiftTimeRepository shiftTimeRepository;

    public PageDTO<AttendanceShiftDTO> list(AttendanceShiftCriteria criteria, Pageable pageable) {
        Specification<AttendanceShift> spec = SpecificationBuilder.builder()
                .where(Criterion.like("name", criteria.getName()))
                .build();
        Page<AttendanceShift> page = attendanceShiftRepository.findAll(spec, pageable);
        List<AttendanceShiftDTO> list = page.getContent().stream().map(item -> {
            AttendanceShiftDTO attendanceShiftDTO = new AttendanceShiftDTO()
                    .setId(item.getId())
                    .setCanBeLate(item.getCanBeLate())
                    .setCanLeaveEarly(item.getCanLeaveEarly())
                    .setLooseTime(item.getLooseTime())
                    .setMaxBeginTime(item.getMaxBeginTime())
                    .setMaxBeginTimeEnable(item.getMaxBeginTimeEnable())
                    .setMaxEndTime(item.getMaxEndTime())
                    .setMaxEndTimeEnable(item.getMaxEndTimeEnable())
                    .setName(item.getName())
                    .setCreateTime(item.getCreateTime())
                    .setUpdateTime(item.getUpdateTime());
            attendanceShiftDTO.setShiftTimeDTOS(item.getShiftTimes().stream().map(time -> {
                AttendanceShiftTimeDTO timeDTO = new AttendanceShiftTimeDTO()
                        .setOffWorkTime(time.getOffWorkTime())
                        .setOffWorkTimeEnd(time.getOffWorkTimeEnd())
                        .setOffWorkTimeStart(time.getOffWorkTimeStart())
                        .setWorkingHours(time.getWorkingHours())
                        .setWorkingHoursEnd(time.getWorkingHoursEnd())
                        .setWorkingHoursStart(time.getWorkingHoursStart());
                return timeDTO;
            }).collect(Collectors.toList()));
            return attendanceShiftDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public AttendanceShift add(AttendanceShiftAddReq req) {

        this.validTimes(req.getTimeDTOS(), req.getLooseTime(), req.getCanBeLate(), req.getCanLeaveEarly(), req.getMaxBeginTimeEnable(), req.getMaxBeginTime(), req.getMaxEndTimeEnable(), req.getMaxEndTime());

        AttendanceShift attendanceShift = new AttendanceShift()
                .setCanBeLate(req.getCanBeLate())
                .setCanLeaveEarly(req.getCanLeaveEarly())
                .setLooseTime(req.getLooseTime())
                .setMaxBeginTime(req.getMaxBeginTime())
                .setMaxBeginTimeEnable(req.getMaxBeginTimeEnable())
                .setMaxEndTime(req.getMaxEndTime())
                .setMaxEndTimeEnable(req.getMaxEndTimeEnable())
                .setName(req.getName());
        attendanceShift = attendanceShiftRepository.save(attendanceShift);
        for (AttendanceShiftTimeDTO timeDTO : req.getTimeDTOS()) {
            AttendanceShiftTime shiftTime = new AttendanceShiftTime()
                    .setOffWorkTime(timeDTO.getOffWorkTime())
                    .setOffWorkTimeEnd(timeDTO.getOffWorkTimeEnd())
                    .setOffWorkTimeStart(timeDTO.getOffWorkTimeStart())
                    .setShift(attendanceShift)
                    .setWorkingHours(timeDTO.getWorkingHours())
                    .setWorkingHoursStart(timeDTO.getWorkingHoursStart())
                    .setWorkingHoursEnd(timeDTO.getWorkingHoursEnd());
            this.shiftTimeRepository.save(shiftTime);
        }
        return attendanceShift;
    }

    private void validTimes(List<AttendanceShiftTimeDTO> timeDTOS, Boolean looseTime, Integer canBeLate, Integer canLeaveEarly, Boolean maxBeginTimeEnable, Integer maxBeginTime, Boolean maxEndTimeEnable, Integer maxEndTime) {
        for (int i = 1; i < timeDTOS.size(); i++) {
            AttendanceShiftTimeDTO preData = timeDTOS.get(Math.max(i - 1, 0));
            AttendanceShiftTimeDTO nowData = timeDTOS.get(i);

            //当前班次与上一班次存在时间交集
            if (preData.getOffWorkTimeEnd().isAfter(nowData.getWorkingHoursStart())) {
                throw new BizException("", "班次" + i + "与班次" + (i + 1) + "打卡时间范围存在重叠");
            }
        }

        for (AttendanceShiftTimeDTO timeDTO : timeDTOS) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            if (timeDTO.getWorkingHoursEnd().isAfter(timeDTO.getOffWorkTimeStart())) {
                throw new BizException("", "打卡时间范围不能重叠");
            }

            if (timeDTO.getWorkingHours().isAfter(timeDTO.getOffWorkTime())) {
                throw new BizException("", "上班时间" + formatter.format(timeDTO.getWorkingHours()) + ",应早于下班时间" + formatter.format(timeDTO.getOffWorkTime()));
            }
            if (timeDTO.getWorkingHours().isBefore(timeDTO.getWorkingHoursStart())) {
                throw new BizException("", "上班时间" + formatter.format(timeDTO.getWorkingHours()) + ",应晚于上班开始打卡时间" + formatter.format(timeDTO.getWorkingHoursStart()));
            }
            if (timeDTO.getWorkingHours().isAfter(timeDTO.getWorkingHoursEnd())) {
                throw new BizException("", "上班时间" + formatter.format(timeDTO.getWorkingHours()) + ",应早于上班结束打卡时间" + formatter.format(timeDTO.getWorkingHoursEnd()));
            }
            if (timeDTO.getOffWorkTime().isBefore(timeDTO.getOffWorkTimeStart())) {
                throw new BizException("", "下班时间" + formatter.format(timeDTO.getOffWorkTime()) + ",应晚于下班开始打卡时间" + formatter.format(timeDTO.getOffWorkTimeStart()));
            }
            if (timeDTO.getOffWorkTime().isAfter(timeDTO.getOffWorkTimeEnd())) {
                throw new BizException("", "下班时间" + formatter.format(timeDTO.getOffWorkTime()) + ",应早于下班结束打卡时间" + formatter.format(timeDTO.getOffWorkTimeEnd()));
            }
        }

        if (looseTime && canBeLate == null && canLeaveEarly == null) {
            throw new BizException("", "请选择可晚到或早走时间");
        }

        if (maxBeginTimeEnable && maxBeginTime == null) {
            throw new BizException("", "请输入严重迟到分钟数");
        }

        if (maxEndTimeEnable && maxEndTime == null) {
            throw new BizException("", "旷工迟到时间");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Object update(AttendanceShiftEditReq req) {

        AttendanceShift attendanceShift = this.attendanceShiftRepository.findById(req.getId()).orElseThrow(() -> new BizException("", "未找到记录"));

        this.validTimes(req.getTimeDTOS(), req.getLooseTime(), req.getCanBeLate(), req.getCanLeaveEarly(), req.getMaxBeginTimeEnable(), req.getMaxBeginTime(), req.getMaxEndTimeEnable(), req.getMaxEndTime());

        attendanceShift.setCanBeLate(req.getCanBeLate())
                .setCanLeaveEarly(req.getCanLeaveEarly())
                .setLooseTime(req.getLooseTime())
                .setMaxBeginTime(req.getMaxBeginTime())
                .setMaxBeginTimeEnable(req.getMaxBeginTimeEnable())
                .setMaxEndTime(req.getMaxEndTime())
                .setMaxEndTimeEnable(req.getMaxEndTimeEnable())
                .setName(req.getName());
        //清除历史班次
        this.shiftTimeRepository.deleteAll(attendanceShift.getShiftTimes());

        attendanceShift = attendanceShiftRepository.save(attendanceShift);

        //新增班次
        for (AttendanceShiftTimeDTO timeDTO : req.getTimeDTOS()) {
            AttendanceShiftTime shiftTime = new AttendanceShiftTime()
                    .setOffWorkTime(timeDTO.getOffWorkTime())
                    .setOffWorkTimeEnd(timeDTO.getOffWorkTimeEnd())
                    .setOffWorkTimeStart(timeDTO.getOffWorkTimeStart())
                    .setShift(attendanceShift)
                    .setWorkingHours(timeDTO.getWorkingHours())
                    .setWorkingHoursStart(timeDTO.getWorkingHoursStart())
                    .setWorkingHoursEnd(timeDTO.getWorkingHoursEnd());
            this.shiftTimeRepository.save(shiftTime);
        }
        return attendanceShift;
    }

    @Transactional(rollbackFor = Exception.class)
    public AttendanceShift delete(Long id) {
        Optional<AttendanceShift> optional = attendanceShiftRepository.findById(id);
        if (optional.isPresent()) {
            AttendanceShift shift = optional.get();
            this.shiftTimeRepository.deleteAll(shift.getShiftTimes());
            attendanceShiftRepository.deleteById(id);
        }
        return optional.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public Object batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择删除项");
        }

        List<AttendanceShift> shiftList = new ArrayList<>();
        AttendanceShift shift;
        for (Long id : ids) {
            shift = attendanceShiftRepository.findById(id).orElseThrow(() -> new BizException("", "班次不存在"));
            shiftList.add(shift);
            this.shiftTimeRepository.deleteAll(shift.getShiftTimes());
        }
        attendanceShiftRepository.deleteAll(shiftList);
        return shiftList;
    }

}