package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.constant.AttendanceGroupTypeEnum;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.constant.RedisConstants;
import com.iotinall.canteen.dto.assessrecord.AssessRecordContentReq;
import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftTimeDTO;
import com.iotinall.canteen.dto.attendancesrecord.*;
import com.iotinall.canteen.dto.employee.EmpListDTO;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.utils.FileUtil;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import com.iotinall.canteen.utils.PersonalType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 考勤记录
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AttendanceRecordService {
    @Resource
    private AttendanceRecordRepository attendanceRecordRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private AttendanceGroupRepository groupRepository;
    @Resource
    private AttendanceGroupMemberRepository groupMemberRepository;
    @Resource
    private AttendanceRecordDetailRepository recordDetailRepository;
    @Resource
    private AttendanceShiftRecordRepository shiftRecordRepository;
    @Resource
    private AttendanceVacateRecordRepository vacateRecordRepository;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private KitchenMorningInspectService kitchenMorningInspectService;
    @Resource
    private FileHandler fileHandler;
    @Resource
    private WorkOvertimeRecordService overtimeRecordService;
    @Resource
    private AttendanceVacateRecordService vacateRecordService;
    @Resource
    private WorkOvertimeApplyRepository applyRepository;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private AttendanceReportRecordRepository attendanceReportRecordRepository;
    @Resource
    private AttendanceShiftTimeRepository attendanceShiftTimeRepository;

    public PageDTO<AttendanceRecordDTO> list(AttendanceRecordCriteria criteria, Pageable pageable) {
        Specification<AttendanceRecord> spec = SpecificationBuilder.builder()
                .whereByOr(
                        Criterion.eq("details.punchInStatus", criteria.getStatus()),
                        Criterion.eq("details.punchOutStatus", criteria.getStatus())
                )
                .where(Criterion.eq("employee.id", criteria.getEmpId()))
                .where(Criterion.like("employee.name", criteria.getName()))
                .where(Criterion.gte("recordDate", criteria.getBeginTime()))
                .where(Criterion.lte("recordDate", criteria.getEndTime()))
                .build();
        Page<AttendanceRecord> page = attendanceRecordRepository.findAll(spec, pageable);

        List<AttendanceRecordDTO> list = page.getContent().stream().map(this::convert).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    public Map<String, Object> findNowUser(AttendanceRecordCriteria criteria) {
        Map<String, Object> map = new HashMap<>();
        Specification<AttendanceRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.gte("recordDate", criteria.getBeginTime()))
                .where(Criterion.lte("recordDate", criteria.getEndTime()))
                .where(Criterion.eq("employee.id", SecurityUtils.getUserId()))
                .build();

        List<AttendanceRecord> records = attendanceRecordRepository.findAll(spec);

        List<AttendanceRecordDTO> list = records.stream().map(this::convert).collect(Collectors.toList());

        Long state = 0L;

        if (criteria.getBeginTime() != null) {
            List<AttendanceVacateRecord> vacateRecords = vacateRecordRepository.findByTime(SecurityUtils.getUserId(), LocalDateTime.of(criteria.getBeginTime(), LocalTime.MIN), LocalDateTime.of(criteria.getBeginTime(), LocalTime.MAX));
            if (!CollectionUtils.isEmpty(vacateRecords)) {
                AttendanceVacateRecord vacateRecord = vacateRecords.get(0);
                if (vacateRecord.getState() == Constants.AUDIT_OK) {
                    map.put("startTime", vacateRecord.getBeginTime() != null ? vacateRecord.getBeginTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
                    map.put("endTime", vacateRecord.getEndTime() != null ? vacateRecord.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
                    state = 2L;
                }
            }

            List<WorkOvertimeApply> timeApplies = this.applyRepository.findApply(SecurityUtils.getUserId(), criteria.getBeginTime());
            if (!timeApplies.isEmpty()) {
                state = 1L;
                WorkOvertimeApply overtimeApply = timeApplies.get(timeApplies.size() - 1);
                map.put("startTime", overtimeApply.getStartTime() != null ? overtimeApply.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
                map.put("endTime", overtimeApply.getEndTime() != null ? overtimeApply.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
            }
        }

        map.put("content", list);
        map.put("total", state);

        return map;
    }

    private AttendanceRecordDTO convert(AttendanceRecord record) {
        AttendanceRecordDTO recordDTO = new AttendanceRecordDTO();
        BeanUtils.copyProperties(record, recordDTO);

        if (record.getEmpId() != null) {
            FeignEmployeeDTO employeeDTO = feignEmployeeService.findById(record.getEmpId());
            if (employeeDTO != null) {
                EmpListDTO emp = new EmpListDTO();
                recordDTO.setEmployee(emp);
                emp.setId(employeeDTO.getId());
                emp.setName(employeeDTO.getName());
            }

        }

        List<AttendanceShiftRecordDTO> detailDTOs = new ArrayList<>();
        AttendanceShiftRecordDTO detailDTO;
        List<AttendanceShiftRecord> details = record.getDetails().stream().sorted(Comparator.comparing(AttendanceShiftRecord::getShiftBeginTime)).collect(Collectors.toList());
        for (AttendanceShiftRecord detail : details) {
            detailDTO = new AttendanceShiftRecordDTO();
            detailDTOs.add(detailDTO);
            BeanUtils.copyProperties(detail, detailDTO);
        }
        recordDTO.setDetails(detailDTOs);
        return recordDTO;
    }

    /**
     * 处理考勤记录
     */
    public void handle(String identityNo, String base64Img, Long recordDate, Float temperature) {
        log.info("--------收到打卡数据--------");
        log.info("身份证：{}", identityNo);
        log.info("体温：{}", temperature);
        LocalDateTime recordDateTime = LocalDateTimeUtil.long2LocalDateTime(recordDate);

        //打卡人员
        FeignSimEmployeeDto employee = feignEmployeeService.findByIdNo(identityNo);
        if (null == employee) {
            log.info("后厨人员不存在，打卡无效");
            return;
        }

        //考勤照片
        String imgUrl = "";
        File file = null;
        try {
            file = FileUtil.base64ToTempFile(base64Img, System.currentTimeMillis() + "");
            imgUrl = fileHandler.saveImage("group1", ImageIO.read(file));
        } catch (Exception ex) {
            log.info("考勤照片转换失败");
        } finally {
            if (null != file && file.exists()) {
                file.delete();
            }
        }

        //上班记录
        AttendanceReportRecord reportRecord = new AttendanceReportRecord();
        reportRecord.setTemperature(temperature);
        reportRecord.setPunchImg(imgUrl);
        reportRecord.setEmpId(employee.getId());
        reportRecord.setCreateTime(recordDateTime);
        reportRecord.setRecordDate(recordDateTime.toLocalDate());
        attendanceReportRecordRepository.save(reportRecord);

        //添加晨检记录
        //temperature = 37f;
        //todo del
        if (temperature != null && temperature.intValue() > 0) {
            kitchenMorningInspectService.addMorningInspectFromAttendanceRecord(employee, recordDateTime, temperature, imgUrl);
        }

        AttendanceGroup group = this.getKitchenEmployeeAttendanceGroup(employee.getId());
        if (null == group) {
            log.info("后厨人员：{}未配置考勤组", employee.getName());
            return;
        }

        Boolean whetherToWorkOvertime = false;
        //根据加班规则生成加班记录
        try {
            whetherToWorkOvertime = this.overtimeRecordService.createRecord(recordDateTime, group, employee);
        } catch (BizException e) {
            log.info("生成加班数据失败：{}", e.getMsg());
        }

        try {
            AttendanceShiftTime newShift = this.getCurrentSimilarShiftsTime(group, recordDateTime, employee.getId());

            AttendanceShift attendanceShift = this.getCurrentSimilarShifts(group, recordDateTime, employee.getId());

            if (null == newShift) {
                log.info("当前非考勤时间段：{}", employee.getName());
                return;
            }


            Boolean isWorkOn = this.isWorkOn(newShift, recordDateTime);

            //当天考勤记录
            AttendanceRecord record = this.attendanceRecordRepository.queryByEmpIdAndRecordDate(employee.getId(), recordDateTime.toLocalDate());
            if (null == record) {
                record = new AttendanceRecord();
                record.setCreateTime(LocalDateTime.now());
                record.setEmpId(employee.getId());
                record.setRecordDate(recordDateTime.toLocalDate());
                record = this.attendanceRecordRepository.save(record);
            }

            //当前班次考勤记录
            List<AttendanceRecordDetail> detailList;
            AttendanceRecordDetail detail = new AttendanceRecordDetail();
            AttendanceShiftRecord shiftRecord = shiftRecordRepository.findByRecordIdAndShiftId(record.getId(), newShift.getId());
            if (null == shiftRecord) {
                shiftRecord = new AttendanceShiftRecord();
                shiftRecord.setRecord(record);
                shiftRecord.setCreateTime(LocalDateTime.now());
                shiftRecord.setShiftBeginTime(newShift.getWorkingHours());
                shiftRecord.setShiftEndTime(newShift.getOffWorkTime());
                shiftRecord.setShiftName(newShift.getShift() != null ? newShift.getShift().getName() : null);
                shiftRecord.setShiftId(newShift.getId());
                shiftRecord.setPunchInStatus(Constants.ATTENDANCE_STATUS_NOT_PUNCH);
                shiftRecord.setPunchOutStatus(Constants.ATTENDANCE_STATUS_NOT_PUNCH);
                shiftRecord = this.shiftRecordRepository.save(shiftRecord);

                detailList = new ArrayList<>(1);

            } else {
                //            shiftRecord.setShiftEndTime(shift.getEndTime());
                detailList = new ArrayList<>(shiftRecord.getDetails());
            }

            detail.setShiftRecordId(shiftRecord.getId());
            detail.setPunchImg(imgUrl);
            detail.setPunchTime(recordDateTime.toLocalTime());
            detail.setPunchTemperature(temperature);
            this.recordDetailRepository.save(detail);

            if (isWorkOn == null) {
                throw new BizException("", "非打卡时间");
            }

            Integer status = this.determineType(newShift, recordDateTime, attendanceShift, whetherToWorkOvertime);
            if (null == status) {
                log.info("当前非考勤时间段");
                return;
            }

            if (isWorkOn) {
                //取打卡最早的记录
                if (shiftRecord.getPunchInStatus() == Constants.ATTENDANCE_STATUS_NOT_PUNCH) {
                    shiftRecord.setPunchInImg(imgUrl);
                    shiftRecord.setPunchInStatus(status);
                    shiftRecord.setPunchInTemperature(temperature);
                    shiftRecord.setPunchInTime(recordDateTime.toLocalTime());
                }
            } else {
                shiftRecord.setPunchOutImg(imgUrl);
                shiftRecord.setPunchOutStatus(status);
                shiftRecord.setPunchOutTime(recordDateTime.toLocalTime());
                shiftRecord.setPunchOutTemperature(temperature);
            }

            detailList.add(detail);
            shiftRecord.setDetails(new HashSet<>(detailList));
            this.shiftRecordRepository.save(shiftRecord);

            List<AttendanceShiftRecord> shiftRecordsList = new ArrayList<>(record.getDetails());
            if (CollectionUtils.isEmpty(shiftRecordsList)) {
                shiftRecordsList = new ArrayList<>(1);
            }

            shiftRecordsList.add(shiftRecord);
            record.setDetails(new HashSet<>(shiftRecordsList));

            this.attendanceRecordRepository.save(record);
        } catch (BizException e) {
            log.error("打卡失败：{}", e.getMsg());
        }
    }

    /**
     * 获取用户所在考勤组
     */
    @Transactional(rollbackFor = Exception.class)
    public AttendanceGroup getKitchenEmployeeAttendanceGroup(Long empId) {
        AttendanceGroupMember groupMember = groupMemberRepository.findByEmpId(empId);
        if (null == groupMember) {
            return null;
        }

        return groupRepository.findById(groupMember.getGroupId()).orElse(null);
    }

    /**
     * 决策
     * 返回<打卡类型，打卡状态>
     */
    private Integer determineType(AttendanceShiftTime shift, LocalDateTime recordDateTime, AttendanceShift attendanceShift, Boolean whetherToWorkOvertime) {
        /**
         * 1-正常 2-迟到 3-早退
         * recordDateTime<maxDelayTime 上班打卡正常
         * maxDelayTime<=recordDateTime<=maxBeginTime 上班打卡迟到
         * maxBeginTime<recordDateTime<minEndTime 下班早退
         * minEndTime<=recordDateTime<=maxEndTime 下班正常 其他时间为该班次缺卡
         */
        LocalTime trueWorkTime = attendanceShift.getLooseTime() ? shift.getWorkingHours().plusMinutes(attendanceShift.getCanBeLate()) : shift.getWorkingHours();
        LocalTime trueOffWorkTime = attendanceShift.getLooseTime() ? shift.getOffWorkTime().minusMinutes(attendanceShift.getCanLeaveEarly()) : shift.getOffWorkTime();
        Integer status = null;
        LocalTime plusTime = recordDateTime.toLocalTime();
        if (plusTime.isBefore(trueWorkTime)) {
            //上班正常
            status = Constants.ATTENDANCE_STATE_NORMAL;
        } else if (plusTime.isAfter(trueWorkTime) && plusTime.isBefore(shift.getWorkingHoursEnd())) {
            //上班打卡迟到
            status = Constants.ATTENDANCE_STATE_LATE;
            //严重迟到
            Long detailTime = Math.abs(Duration.between(plusTime, trueWorkTime).toMinutes());
            if (attendanceShift.getMaxBeginTimeEnable() && attendanceShift.getMaxBeginTime() < detailTime) {
                status = Constants.ATTENDANCE_STATE_LATE_SERIOUS;
            }
            //旷工迟到
            if (attendanceShift.getMaxEndTimeEnable() && attendanceShift.getMaxEndTime() < detailTime) {
                status = Constants.ATTENDANCE_STATE_MISS;
            }
        } else if (plusTime.isAfter(shift.getOffWorkTimeStart()) && plusTime.isBefore(trueOffWorkTime)) {
            //早退
            status = Constants.ATTENDANCE_STATE_EARLY;
        } else if (plusTime.isAfter(trueOffWorkTime) && plusTime.isBefore(shift.getOffWorkTimeEnd())) {
            //下班正常
            status = Constants.ATTENDANCE_STATE_NORMAL;
            //是否加班
            if (whetherToWorkOvertime) {
                status = Constants.ATTENDANCE_OVERTIME;
            }
        }

        return status;
    }

    /**
     * 获取当前考勤所有班次
     */
    private AttendanceGroupShift getShifts(AttendanceGroup group, LocalDateTime recordDate) {
        int dayOfWeek = recordDate.getDayOfWeek().getValue();

        AttendanceGroupShift groupShift = group.getShiftList().stream().filter(item -> item.getWeekday() == dayOfWeek).findFirst().orElseThrow(() -> new BizException("", "未找到周配置"));

        return groupShift;
    }

    /**
     * 判断是否是上班
     *
     * @param shift
     * @return
     */
    private Boolean isWorkOn(AttendanceShiftTime shift, LocalDateTime recordDate) {
        LocalTime recordTime = recordDate.toLocalTime();
        if ((recordTime.isAfter(shift.getWorkingHoursStart()) && recordTime.isBefore(shift.getWorkingHoursEnd()))) {
            return Boolean.TRUE;
        }

        if (recordTime.isAfter(shift.getOffWorkTimeStart()) && recordTime.isBefore(shift.getOffWorkTimeEnd())) {
            return Boolean.FALSE;
        }

        return null;
    }

    /**
     * 当前用户考勤班次
     *
     * @return
     */
    public Map<String, Object> findShiftTime(LocalDate date) {
        Map<String, Object> result = new HashMap<>();
        FeignSimEmployeeDto employee = feignEmployeeService.findSimById(SecurityUtils.getUserId());
        if (null == employee) {
            throw new BizException("未找到用户");
        }

        WorkOvertimeApply apply = applyRepository.findApplyAll(employee.getId(), date);
        result.put("applyed", apply == null);
        AttendanceGroup attendanceGroup = this.getKitchenEmployeeAttendanceGroup(employee.getId());
        if (attendanceGroup == null) {
            result.put("shifts", Collections.emptyList());
            return result;
        }
        AttendanceShift attendanceShift;
        try {
            attendanceShift = this.getCurrentSimilarShifts(attendanceGroup, LocalDateTime.of(date, LocalTime.MIN), employee.getId());

        } catch (BizException e) {
            result.put("shifts", Collections.emptyList());
            return result;
        }
        if (attendanceShift == null) {
            result.put("shifts", Collections.emptyList());
            return result;
        }
        result.put("shifts", attendanceShift.getShiftTimes().stream().map(item -> {
            AttendanceShiftTimeDTO timeDTO = new AttendanceShiftTimeDTO()
                    .setId(item.getId())
                    .setWorkingHoursStart(item.getWorkingHoursStart())
                    .setWorkingHours(item.getWorkingHours())
                    .setWorkingHoursEnd(item.getWorkingHoursEnd())
                    .setOffWorkTimeStart(item.getOffWorkTimeStart())
                    .setOffWorkTimeEnd(item.getOffWorkTimeEnd())
                    .setOffWorkTime(item.getOffWorkTime());
            return timeDTO;
        }).collect(Collectors.toList()));
        return result;
    }

    /**
     * 获取后厨人员最接近的的班次,返回是上班打卡还是下班打卡
     */
    public AttendanceShiftTime getCurrentSimilarShiftsTime(AttendanceGroup group, LocalDateTime recordDate, Long empId) {
        LocalTime recordTime = recordDate.toLocalTime();
        switch (AttendanceGroupTypeEnum.findByCode(group.getType())) {
            case FIXED: {
                //固定班制
                AttendanceGroupShift groupShift = this.getShifts(group, recordDate);
                AttendanceShift attendanceShift = groupShift.getShifts();
                if (attendanceShift == null) {
                    throw new BizException("", "当天休息");
                }
                for (AttendanceShiftTime shift : attendanceShift.getShiftTimes()) {
                    if ((recordTime.isAfter(shift.getWorkingHoursStart()) && recordTime.isBefore(shift.getWorkingHoursEnd()))
                            || (recordTime.isAfter(shift.getOffWorkTimeStart()) && recordTime.isBefore(shift.getOffWorkTimeEnd()))) {
                        return shift;
                    }
                }
                break;
            }
            case SHIFT_SYSTEM: {
                //排班制
                Set<AttendanceGroupShiftSystem> groupShiftSystems = group.getShiftSystems();
                if (CollectionUtils.isEmpty(groupShiftSystems)) {
                    throw new BizException("", "当前时间未排班");
                }
                AttendanceGroupShiftSystem shiftSystem = groupShiftSystems
                        .stream()
                        .filter(item -> item.getEmpId() != null && Objects.equals(item.getEmpId(), empId))
                        .findFirst()
                        .orElseThrow(() -> new BizException("未找到当前用户排班信息"));
                List<AttendanceGroupShiftSystemDate> shiftSystemDates = shiftSystem.getShiftSystemDateList();
                if (CollectionUtils.isEmpty(shiftSystemDates)) {
                    throw new BizException("", "当前用户未排班");
                }
                AttendanceGroupShiftSystemDate shiftSystemDate = shiftSystemDates.stream().filter(item -> Objects.equals(item.getDate(), recordDate.toLocalDate())).findFirst().orElseThrow(() -> new BizException("", "未找到当前日期排班信息"));

                AttendanceShift attendanceShift = shiftSystemDate.getShift();
                if (attendanceShift == null) {
                    throw new BizException("", "当天未排班");
                }
                for (AttendanceShiftTime shift : attendanceShift.getShiftTimes()) {
                    if ((recordTime.isAfter(shift.getWorkingHoursStart()) && recordTime.isBefore(shift.getWorkingHoursEnd()))
                            || (recordTime.isAfter(shift.getOffWorkTimeStart()) && recordTime.isBefore(shift.getOffWorkTimeEnd()))) {
                        return shift;
                    }
                }
                break;
            }
            default: {
                throw new BizException("", "未知考勤类型");
            }
        }

        return null;
    }

    public AttendanceShift getCurrentSimilarShifts(AttendanceGroup group, LocalDateTime recordDate, Long empId) {
        switch (AttendanceGroupTypeEnum.findByCode(group.getType())) {
            case FIXED: {
                //固定班制
                AttendanceGroupShift groupShift = this.getShifts(group, recordDate);
                AttendanceShift attendanceShift = groupShift.getShifts();
                return attendanceShift;
            }
            case SHIFT_SYSTEM: {
                //排班制
                Set<AttendanceGroupShiftSystem> groupShiftSystems = group.getShiftSystems();
                if (CollectionUtils.isEmpty(groupShiftSystems)) {
                    throw new BizException("", "当前时间未排班");
                }
                AttendanceGroupShiftSystem shiftSystem = groupShiftSystems.stream().filter(item -> item.getEmpId() != null && Objects.equals(item.getEmpId(), empId)).findFirst().orElseThrow(() -> new BizException("", "未找到当前用户排班信息"));
                List<AttendanceGroupShiftSystemDate> shiftSystemDates = shiftSystem.getShiftSystemDateList();
                if (CollectionUtils.isEmpty(shiftSystemDates)) {
                    throw new BizException("", "当前用户未排班");
                }
                AttendanceGroupShiftSystemDate shiftSystemDate = shiftSystemDates.stream().filter(item -> Objects.equals(item.getDate(), recordDate.toLocalDate())).findFirst().orElseThrow(() -> new BizException("", "未找到当前日期排班信息"));

                AttendanceShift attendanceShift = shiftSystemDate.getShift();
                return attendanceShift;
            }
            default: {
                throw new BizException("", "未知考勤类型");
            }
        }

    }


    /**
     * 创建每个人明天的考勤任务
     */
    public void createNextDayAttendanceRecord() {
        //所有的后厨人员
        List<FeignSimEmployeeDto> employeeList = feignEmployeeService.findAllByType(PersonalType.BACKKITCHEN.getCode(), Boolean.FALSE);

        if (CollectionUtils.isEmpty(employeeList)) {
            return;
        }

        LocalDateTime recordDateTime = LocalDateTime.now();
        AttendanceRecord record;
        AttendanceShiftRecord shiftRecord;
        for (FeignSimEmployeeDto employee : employeeList) {
            //获取该员工所在的考勤组
            AttendanceGroup group = this.getKitchenEmployeeAttendanceGroup(employee.getId());
            if (null == group) {
                continue;
            }
            AttendanceShift shifts = null;
            try {
                shifts = this.getCurrentSimilarShifts(group, recordDateTime, employee.getId());
            } catch (Exception ignored) {
            }

            if (shifts == null) {
                continue;
            }

            record = this.attendanceRecordRepository.findByEmpIdAndRecordDate(employee.getId(), LocalDate.now());
            if (null != record) {
                continue;
            }

            record = new AttendanceRecord();
            record.setRecordDate(recordDateTime.toLocalDate());
            record.setEmpId(employee.getId());
            record.setCreateTime(recordDateTime);
            record = this.attendanceRecordRepository.save(record);

            Set<Long> shiftIds = new HashSet<>();
            for (AttendanceShiftTime s : shifts.getShiftTimes()) {
                if (!shiftIds.add(s.getId())) {
                    continue;
                }
                shiftRecord = new AttendanceShiftRecord();
                shiftRecord.setPunchInStatus(Constants.ATTENDANCE_STATUS_NOT_PUNCH);
                shiftRecord.setPunchOutStatus(Constants.ATTENDANCE_STATUS_NOT_PUNCH);
                shiftRecord.setShiftId(s.getId());
                shiftRecord.setShiftName(shifts.getName());
                shiftRecord.setShiftBeginTime(s.getWorkingHours());
                shiftRecord.setShiftEndTime(s.getOffWorkTime());
                shiftRecord.setRecord(record);
                this.shiftRecordRepository.save(shiftRecord);
            }
        }
    }

    /**
     * 更新打卡记录状态
     */
    public void taskUpdateAttendanceRecordStatus() {
        List<AttendanceShiftRecord> shiftRecordList = this.shiftRecordRepository.findByPunchInStatusOrPunchOutStatus(Constants.ATTENDANCE_STATUS_NOT_PUNCH, Constants.ATTENDANCE_STATUS_NOT_PUNCH);
        if (CollectionUtils.isEmpty(shiftRecordList)) {
            return;
        }

        List<AttendanceShiftRecord> shiftRecords = new ArrayList<>();
        for (AttendanceShiftRecord shiftRecord : shiftRecordList) {
            if (shiftRecord.getPunchInStatus() != Constants.ATTENDANCE_STATUS_NOT_PUNCH
                    && shiftRecord.getPunchOutStatus() != Constants.ATTENDANCE_STATUS_NOT_PUNCH) {
                continue;
            }

            if (shiftRecord.getPunchInStatus() == Constants.ATTENDANCE_STATUS_NOT_PUNCH) {
                /**
                 *上班未打卡
                 */
                shiftRecord.setPunchInStatus(this.judgeAttendanceStatus(shiftRecord));
            }

            if (shiftRecord.getPunchOutStatus() == Constants.ATTENDANCE_STATUS_NOT_PUNCH) {
                /**
                 *下班未打卡
                 */
                shiftRecord.setPunchOutStatus(this.judgeAttendanceStatus(shiftRecord));
            }
            shiftRecords.add(shiftRecord);
        }

        this.shiftRecordRepository.saveAll(shiftRecords);
    }

    /**
     * 判断打卡记录的状态
     */
    private Integer judgeAttendanceStatus(AttendanceShiftRecord shiftRecord) {
        AttendanceRecord attendanceRecord = this.attendanceRecordRepository.findById(shiftRecord.getRecord().getId()).orElse(null);
        if (null != attendanceRecord) {
            LocalDate recordDate = attendanceRecord.getRecordDate();
            LocalDateTime beginTime = LocalDateTime.of(recordDate, shiftRecord.getShiftBeginTime());
            LocalDateTime endTime = LocalDateTime.of(recordDate, shiftRecord.getShiftEndTime());

            List<AttendanceVacateRecord> vacateRecordList = this.vacateRecordRepository.findByBeginTimeAndEndTime(attendanceRecord.getEmpId(), beginTime, endTime);

            /**
             * 存在请假记录，且审批状态为通过，更新打卡状态为请假
             */
            if (!CollectionUtils.isEmpty(vacateRecordList)) {
                AttendanceVacateRecord vacateRecord = vacateRecordList.get(0);
                if (vacateRecord.getState() == Constants.AUDIT_OK) {
                    return Constants.ATTENDANCE_STATE_VACATE;
                }
            }
        }
        return Constants.ATTENDANCE_STATE_MISS;
    }

    /**
     * 请假申请通过更新考勤记录状态
     * 1。请假日期第二天凌晨更新考勤记录状态
     * 2.后面补的申请记录，直接更新对应的考勤记录状态
     */
    public void auditUpdateAttendanceRecordStatus(Long empId, LocalDateTime begin, LocalDateTime end) {
        LocalDateTime optTime = LocalDateTime.now();

        //获取开始时间和结束时间包含的天数
        List<ImmutablePair<LocalDateTime, LocalDateTime>> daysList = this.getUntilList(begin, end);

        LocalDateTime beginDateTime;
        LocalDateTime endDateTime;
        for (ImmutablePair<LocalDateTime, LocalDateTime> day : daysList) {
            beginDateTime = day.left;
            endDateTime = day.right;

            if (endDateTime.toLocalDate().isBefore(optTime.toLocalDate())) {
                //请假的日期已过，直接更新打卡记录
                this.updateAttendanceRecordStatus(empId, beginDateTime, endDateTime);
            } else {
                //今天或者以后，生成定时任务，定时任务每天23:59:59 执行，更新请假状态
                this.createUpdateAttendanceRecordStatus(empId, beginDateTime, endDateTime);
            }
        }
    }

    /**
     * 创建定时更新打卡记录为请假的定时任务
     */
    private void createUpdateAttendanceRecordStatus(Long empId, LocalDateTime beginTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        String key = RedisConstants.EVENT_LEAVE + empId + System.currentTimeMillis();
        redisTemplate.opsForValue().set(key, empId + "");
        redisTemplate.expire(key, LocalDateTime.now().until(endTime.withHour(23).withMinute(0).withSecond(0), ChronoUnit.MINUTES), TimeUnit.MINUTES);

        //保存请假人信息
        Map<String, Object> data = new HashMap<>(3);
        data.put("empId", empId);
        data.put("beginTime", beginTime);
        data.put("endTime", endTime);
        redisTemplate.opsForValue().set(key + ":value", data);
    }

    /**
     * 更新打卡记录为请假
     */
    private void updateAttendanceRecordStatus(Long empId, LocalDateTime beginDate, LocalDateTime endDate) {
        List<AttendanceShiftRecord> recordList = this.attendanceRecordRepository.findEmpVacateRecord(empId, beginDate.toLocalDate(), beginDate.toLocalTime(), endDate.toLocalTime());
        if (CollectionUtils.isEmpty(recordList)) {
            return;
        }

        LocalDateTime shiftBeginTime;
        LocalDateTime shiftEndTime;
        LocalDate recordDate = beginDate.toLocalDate();
        for (AttendanceShiftRecord record : recordList) {
            shiftBeginTime = LocalDateTime.of(recordDate, record.getShiftBeginTime());
            shiftEndTime = LocalDateTime.of(recordDate, record.getShiftEndTime());

            if (beginDate.isBefore(shiftBeginTime) || beginDate.isEqual(shiftBeginTime)) {
                //上班请假
                record.setPunchInStatus(Constants.ATTENDANCE_STATE_VACATE);
            } else if (endDate.isAfter(shiftEndTime) || endDate.isEqual(shiftEndTime)) {
                //下班请假
                record.setPunchInStatus(Constants.ATTENDANCE_STATE_VACATE);
            }

            this.shiftRecordRepository.save(record);
        }
    }

    /**
     * 获取请假时间段相隔的日期
     */
    private List<ImmutablePair<LocalDateTime, LocalDateTime>> getUntilList(LocalDateTime start, LocalDateTime end) {
        if (start.toLocalDate().isEqual(end.toLocalDate())) {
            return Collections.singletonList(new ImmutablePair(start, end));
        }

        Long until = start.until(end, ChronoUnit.DAYS);
        List<ImmutablePair<LocalDateTime, LocalDateTime>> daysList = new ArrayList<>();

        daysList.add(new ImmutablePair(start, start.withHour(23).withMinute(59).withSecond(59)));
        for (int index = 0; index < until.intValue(); index++) {
            LocalDateTime day = start.plusDays(1);
            daysList.add(new ImmutablePair(day.withHour(0).withMinute(0).withSecond(0), start.withHour(23).withMinute(59).withSecond(59)));
        }
        daysList.add(new ImmutablePair(end.withHour(0).withMinute(0).withSecond(0), end));

        return daysList;
    }

    /**
     * 处理请假任务事件
     */
    public void handleLeaveEvent(Long empId, LocalDateTime beginTime, LocalDateTime endTime) {
        this.updateAttendanceRecordStatus(empId, beginTime, endTime);
    }

    /**
     * 当天合格率
     *
     * @return
     */
    public BigDecimal todayPassRate() {
        List<AttendanceRecord> record = this.attendanceRecordRepository.queryByRecordDate(LocalDate.now());
        if (CollectionUtils.isEmpty(record)) {
            return BigDecimal.ZERO;
        }
        List<AttendanceShiftRecord> records = new ArrayList<>();
        for (AttendanceRecord attendanceRecord : record) {
            records.addAll(attendanceRecord.getDetails());
        }
        if (CollectionUtils.isEmpty(records)) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(records.stream().filter(item -> item.getPunchOutStatus() == 1).count() * 100.0 / records.size());
        }
    }

    /**
     * 导出
     *
     * @author loki
     * @date 2019/10/15 15:22
     */
    public void export(HttpServletResponse response, AttendanceRecordCriteria criteria) {
        Specification<AttendanceRecord> spec = SpecificationBuilder.builder()
                .whereByOr(
                        Criterion.eq("details.punchInStatus", criteria.getStatus()),
                        Criterion.eq("details.punchOutStatus", criteria.getStatus())
                )
                .where(Criterion.eq("employee.id", criteria.getEmpId()))
                .where(Criterion.like("employee.name", criteria.getName()))
                .where(Criterion.gte("recordDate", criteria.getBeginTime()))
                .where(Criterion.lte("recordDate", criteria.getEndTime()))
                .build();
        List<AttendanceRecord> result = this.attendanceRecordRepository.findAll(spec);

    }

//    /**
//     * 转换导出数据
//     *
//     * @author loki
//     * @date 2020/12/30 15:17
//     */
//    private Object convertExportData(List<AttendanceRecord> data) {
//        List<AttendanceRecordExport> exportList = new ArrayList<>();
//        AttendanceRecordExport export;
//        for (AttendanceRecord record : data) {
//            export = new AttendanceRecordExport();
//            exportList.add(export);
//            export.setName(record.getEmployee().getName());
//            export.setRole(record.getEmployee().getRole());
//            export.setRecordDate(LocalDateUtil.format(record.getRecordDate()));
//
//            List<String> shiftNameList = new ArrayList<>(record.getDetails().size());
//            List<String> punchInTimeList = new ArrayList<>(record.getDetails().size());
//            List<String> punchInStatus = new ArrayList<>(record.getDetails().size());
//            List<String> punchOutTimeList = new ArrayList<>(record.getDetails().size());
//            List<String> punchOutStatus = new ArrayList<>(record.getDetails().size());
//            for (AttendanceShiftRecord shiftRecord : record.getDetails()) {
//                //班次
//                shiftNameList.add(shiftRecord.getShiftName() + ":" + shiftRecord.getShiftBeginTime() + "-" + shiftRecord.getShiftEndTime());
//
//                //上班打卡时间
//                punchInTimeList.add(null == shiftRecord.getPunchInTime() ? "--:--:--" : shiftRecord.getPunchInTime().toString());
//
//                //上班打卡结果
//                punchInStatus.add(AttendanceRecordStatusEnum.findValueByCode(shiftRecord.getPunchInStatus()));
//
//                //下班打卡时间
//                punchOutTimeList.add(null == shiftRecord.getPunchOutTime() ? "--:--:--" : shiftRecord.getPunchOutTime().toString());
//
//                //下班打卡结果
//                punchOutStatus.add(AttendanceRecordStatusEnum.findValueByCode(shiftRecord.getPunchOutStatus()));
//            }
//            export.setShiftName(String.join("\n", shiftNameList));
//            export.setPunchInTime(String.join("\n", punchInTimeList));
//            export.setPunchInStatus(String.join("\n", punchInStatus));
//            export.setPunchOutTime(String.join("\n", punchOutTimeList));
//            export.setPunchOutStatus(String.join("\n", punchOutStatus));
//
//        }
//        return exportList;
//    }

    /**
     * 导出合计
     *
     * @author hjj
     */
    public void exportSum(HttpServletResponse response, AttendanceRecordCriteria criteria) {
        Specification<AttendanceRecord> spec = SpecificationBuilder.builder()
                .whereByOr(
                        Criterion.eq("details.punchInStatus", criteria.getStatus()),
                        Criterion.eq("details.punchOutStatus", criteria.getStatus())
                )
                .where(Criterion.eq("employee.id", criteria.getEmpId()))
                .where(Criterion.like("employee.name", criteria.getName()))
                .where(Criterion.gte("recordDate", criteria.getBeginTime()))
                .where(Criterion.lte("recordDate", criteria.getEndTime()))
                .build();
        List<AttendanceRecord> result = this.attendanceRecordRepository.findAll(spec);

    }

//    private Object sumExportData(List<AttendanceRecord> list) {
//        List<AttendanceRecordSumExport> sumExports = new ArrayList<>();
//        AttendanceRecordSumExport sumExport;
//        for (AttendanceRecord record : list) {
//            sumExport = new AttendanceRecordSumExport();
//            sumExports.add(sumExport);
//            sumExport.setName(record.getEmployee().getName());
//            sumExport.setRole(record.getEmployee().getRole());
//
//            //上班打卡
//            int noClockIn = 0;
//            int normal = 0;
//            int late = 0;
//            int leaveEarly = 0;
//            int cardMissing = 0;
//            int workOvertime = 0;
//            int leave = 0;
//            int seriousLateness = 0;
//
//            //下班打卡
//            int noClockInOut = 0;
//            int normalOut = 0;
//            int leaveEarlyOut = 0;
//            int cardMissingOut = 0;
//            int workOvertimeOut = 0;
//            int leaveOut = 0;
//
//            for (AttendanceShiftRecord shiftRecord : record.getDetails()) {
//                switch (shiftRecord.getPunchInStatus()) {
//                    case Constants.ATTENDANCE_STATUS_NOT_PUNCH:
//                        noClockIn++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_NORMAL:
//                        normal++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_LATE:
//                        late++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_LATE_SERIOUS:
//                        seriousLateness++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_EARLY:
//                        leaveEarly++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_MISS:
//                        cardMissing++;
//                        break;
//                    case Constants.ATTENDANCE_OVERTIME:
//                        workOvertime++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_VACATE:
//                        leave++;
//                        break;
//                    default:
//                        break;
//                }
//
//                switch (shiftRecord.getPunchOutStatus()) {
//                    case Constants.ATTENDANCE_STATUS_NOT_PUNCH:
//                        noClockInOut++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_NORMAL:
//                        normalOut++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_EARLY:
//                        leaveEarlyOut++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_MISS:
//                        cardMissingOut++;
//                        break;
//                    case Constants.ATTENDANCE_OVERTIME:
//                        workOvertimeOut++;
//                        break;
//                    case Constants.ATTENDANCE_STATE_VACATE:
//                        leaveOut++;
//                        break;
//                    default:
//                        break;
//                }
//            }
//            sumExport.setNoClockIn(String.valueOf(noClockIn));
//            sumExport.setNormal(String.valueOf(normal));
//            sumExport.setLate(String.valueOf(late));
//            sumExport.setSeriousLateness(String.valueOf(seriousLateness));
//            sumExport.setLeaveEarly(String.valueOf(leaveEarly));
//            sumExport.setWorkOvertime(String.valueOf(workOvertime));
//            sumExport.setLeave(String.valueOf(leave));
//            sumExport.setCardMissing(String.valueOf(cardMissing));
//
//            sumExport.setNoClockInOut(String.valueOf(noClockInOut));
//            sumExport.setNormalOut(String.valueOf(normalOut));
//            sumExport.setLeaveEarlyOut(String.valueOf(leaveEarlyOut));
//            sumExport.setCardMissingOut(String.valueOf(cardMissingOut));
//            sumExport.setWorkOvertimeOut(String.valueOf(workOvertimeOut));
//            sumExport.setLeaveOut(String.valueOf(leaveOut));
//        }
//        return sumExports;
//    }

    /**
     * 统计时间范围考勤
     *
     * @return
     */
    public List<AttendanceRecordReckonDto> recordReckonList(AssessRecordContentReq contentReq) {
        Map<Long, FeignEmployeeDTO> employees = this.feignEmployeeService.findByIds(new HashSet<>(contentReq.getEmpIds()));

        List<AttendanceRecordReckonDto> recordReckonDtos = new ArrayList<>(employees.size());
        FeignEmployeeDTO employee;
        for (Long empId : contentReq.getEmpIds()) {
            employee = this.feignEmployeeService.findById(empId);
            if (null == employee) {
                throw new BizException("人员不存在");
            }

            AttendanceRecordReckonDto recordReckonDto = new AttendanceRecordReckonDto()
                    .setEmpId(empId)
                    .setEmpName(employee.getName())
                    .setEmpRole(employee.getPosition());
            AttendanceGroup group = this.getKitchenEmployeeAttendanceGroup(empId);
            if (group == null) {
                recordReckonDtos.add(recordReckonDto);
                continue;
            }
            //获取工作天数
            recordReckonDto.setWorkDays(this.workDay(contentReq.getBeginDate(), contentReq.getEndDate(), group, empId));
            //请假时长（小时）
            Long timeOff = this.vacateRecordService.timeOff(employee.getId(), LocalDateTime.of(contentReq.getBeginDate(), LocalTime.MIN), LocalDateTime.of(contentReq.getEndDate(), LocalTime.MAX));
            recordReckonDto.setAskForLeave(timeOff);
            //加班时长（小时）
            Long overtime = this.overtimeRecordService.overtime(empId, LocalDateTime.of(contentReq.getBeginDate(), LocalTime.MIN), LocalDateTime.of(contentReq.getEndDate(), LocalTime.MAX));
            recordReckonDto.setOvertime(overtime);
            recordReckonDtos.add(recordReckonDto);
        }

        this.addLaterBackData(contentReq.getEmpIds(), contentReq.getBeginDate(), contentReq.getEndDate(), recordReckonDtos);

        return recordReckonDtos;
    }

    /**
     * 添加 迟到早退旷工次数
     */
    private void addLaterBackData(List<Long> employees, LocalDate startTime, LocalDate endTime, List<AttendanceRecordReckonDto> recordReckonDtos) {
        String sql = "SELECT " +
                " d.emp_id empId, " +
                " count(if(sd.punch_in_status = 2, 1, null)) inLater, " +
                " count(if(sd.punch_out_status = 2, 1, null)) outLater, " +
                " count(if(sd.punch_in_status = 3, 1, null)) inEarly, " +
                " count(if(sd.punch_out_status = 3,1, null)) outEarly, " +
                " count(if(sd.punch_in_status = 4, 1, null)) inMiss, " +
                " count(if(sd.punch_out_status = 4, 1, null)) outMiss, " +
                " count(if(sd.punch_in_status = 22, 1, null)) inLaterSer, " +
                " count(if(sd.punch_out_status = 22,1, null)) outLaterSer " +
                " FROM " +
                " attendance_record d, " +
                " attendance_shift_record sd " +
                " WHERE " +
                " d.id = sd.record_id " +
                " AND d.emp_id IN ('" + employees.stream().map(item -> item + "").collect(Collectors.joining("','")) + "') " +
                " AND d.record_date >= ? AND d.record_date <= ? " +
                " AND ( " +
                " sd.punch_in_status IN ( 2, 3, 4, 22 ) " +
                " OR sd.punch_out_status IN ( 2, 3, 4, 22 ) " +
                ") " +
                " GROUP BY d.emp_id";
        List<AttendanceRecordStateDto> stateDtos = jdbcTemplate.query(sql, new Object[]{startTime, endTime}, new BeanPropertyRowMapper<>(AttendanceRecordStateDto.class));

        for (AttendanceRecordReckonDto recordReckonDto : recordReckonDtos) {
            Optional<AttendanceRecordStateDto> stateDto = stateDtos.stream().filter(item -> Objects.equals(recordReckonDto.getEmpId(), item.getEmpId())).findFirst();
            if (stateDto.isPresent()) {
                AttendanceRecordStateDto recordStateDto = stateDto.get();
                recordReckonDto.setBeLate(recordStateDto.getInLater() + recordStateDto.getOutLater())
                        .setLeaveEarly(recordStateDto.getInEarly() + recordStateDto.getOutEarly())
                        .setAbsenteeism(recordStateDto.getInMiss() + recordStateDto.getOutMiss())
                        .setBeLateSer(recordStateDto.getInLaterSer() + recordStateDto.getOutLaterSer());
            }
        }
    }

    /**
     * 工作多少天
     */
    private Integer workDay(LocalDate startTime, LocalDate endTime, AttendanceGroup group, Long empId) {
        Integer days = Long.valueOf(Math.abs(Period.between(startTime, endTime).getDays())).intValue();

        AttendanceGroupTypeEnum typeEnum = AttendanceGroupTypeEnum.findByCode(group.getType());

        Integer count = 0;
        switch (typeEnum) {
            case FIXED: {
                List<Integer> workWeek = group.getShiftList().stream().filter(item -> item.getShifts() != null).map(AttendanceGroupShift::getWeekday).collect(Collectors.toList());

                for (Integer i = 0; i <= days; i++) {
                    LocalDate nowday = i != 0 ? startTime.plusDays(i) : startTime;
                    Integer weekValue = nowday.getDayOfWeek().getValue();
                    if (workWeek.contains(weekValue)) {
                        count++;
                    }
                }
                break;
            }
            case SHIFT_SYSTEM: {
                List<LocalDate> workDay = new ArrayList<>();
                Optional<AttendanceGroupShiftSystem> shiftSystem = group.getShiftSystems().stream().filter(item -> Objects.equals(empId, item.getEmpId())).findFirst();
                if (shiftSystem.isPresent()) {
                    workDay = shiftSystem.get().getShiftSystemDateList().stream().filter(date -> date.getShift() != null).map(AttendanceGroupShiftSystemDate::getDate).collect(Collectors.toList());
                }

                for (Integer i = 0; i <= days; i++) {
                    LocalDate nowday = i != 0 ? startTime.plusDays(i) : startTime;
                    if (workDay.contains(nowday)) {
                        count++;
                    }
                }
                break;
            }
        }

        return count;
    }
}
