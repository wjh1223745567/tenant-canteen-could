package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constant.AttendanceGroupTypeEnum;
import com.iotinall.canteen.dto.attendancegroup.*;
import com.iotinall.canteen.dto.attendanceroster.AttendanceGroupSystemAddReq;
import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftDTO;
import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftTimeDTO;
import com.iotinall.canteen.dto.holiday.FeignHolidayDTO;
import com.iotinall.canteen.dto.holiday.HolidayDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤组 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AttendanceGroupService {
    @Resource
    private AttendanceGroupRepository groupRepository;
    @Resource
    private AttendanceShiftRepository shiftRepository;
    @Resource
    private AttendanceGroupShiftRepository groupShiftRepository;
    @Resource
    private AttendanceGroupMemberRepository groupMemberRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private AttendanceGroupShiftSystemRepository shiftSystemRepository;
    @Resource
    private AttendanceGroupShiftSystemDateRepository shiftSystemDateRepository;
    @Resource
    private FeignHolidayService feignHolidayService;
    @Resource
    private WorkOvertimeConfigRepository configRepository;

    public PageDTO<AttendanceGroupDTO> list(AttendanceGroupCriteria criteria, Pageable pageable) {
        Specification<AttendanceGroup> spec = SpecificationBuilder.builder()
                .where(Criterion.like("name", criteria.getName()))
                .build();
        Page<AttendanceGroup> page = groupRepository.findAll(spec, pageable);
        List<AttendanceGroupDTO> list = page.getContent().stream().map(item -> {
            AttendanceGroupDTO attendanceGroupDTO = new AttendanceGroupDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, attendanceGroupDTO);
            attendanceGroupDTO.setOvertimeId(item.getOvertimeConfig() != null ? item.getOvertimeConfig().getId() : null);
            List<AttendanceGroupDTO.GroupShiftDTO> shiftList = buildGroupShiftList(item.getShiftList());
            attendanceGroupDTO.setShiftList(shiftList.stream().sorted(Comparator.comparing(AttendanceGroupDTO.GroupShiftDTO::getWeekday)).collect(Collectors.toList()));
            return attendanceGroupDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    private List<AttendanceGroupDTO.GroupShiftDTO> buildGroupShiftList(Set<AttendanceGroupShift> shifts) {
        return shifts.stream().map(shift -> {
            AttendanceGroupDTO.GroupShiftDTO groupShiftDTO = new AttendanceGroupDTO.GroupShiftDTO();
            groupShiftDTO.setWeekday(shift.getWeekday());
            AttendanceShiftDTO collect = new AttendanceShiftDTO();
            if (shift.getShifts() != null) {
                BeanUtils.copyProperties(shift.getShifts(), collect);
                collect.setShiftTimeDTOS(shift.getShifts().getShiftTimes().stream().distinct().map(detail -> {
                    AttendanceShiftTimeDTO shiftDTO = new AttendanceShiftTimeDTO();
                    BeanUtils.copyProperties(detail, shiftDTO);
                    return shiftDTO;
                }).sorted(Comparator.comparing(AttendanceShiftTimeDTO::getId)).collect(Collectors.toList()));
            }
            groupShiftDTO.setDetails(collect);
            return groupShiftDTO;
        }).collect(Collectors.toList());
    }

    private List<AttendanceGroupDetailDTO.GroupMemberDTO> buildMemberList(Long groupId) {
        List<AttendanceGroupMember> byGroupId = groupMemberRepository.findByGroupId(groupId);

        return byGroupId.stream().map(item -> {
            AttendanceGroupDetailDTO.GroupMemberDTO memberDTO = new AttendanceGroupDetailDTO.GroupMemberDTO();
            memberDTO.setId(item.getEmpId());

            FeignEmployeeDTO employeeDTO = feignEmployeeService.findById(item.getEmpId());
            if (employeeDTO != null) {
                memberDTO.setName(employeeDTO.getName());
            }
            return memberDTO;
        }).collect(Collectors.toList());
    }

    public AttendanceGroupDetailDTO details(Long id) {
        Optional<AttendanceGroup> byId = groupRepository.findById(id);
        if (!byId.isPresent()) {
            throw new BizException("", "记录不存在");
        }
        AttendanceGroup group = byId.get();
        AttendanceGroupDetailDTO detailDTO = new AttendanceGroupDetailDTO();
        detailDTO.setId(group.getId());
        detailDTO.setHolidays(group.getHolidays());
        detailDTO.setEmpCount(group.getEmpCount());
        detailDTO.setType(group.getType());
        detailDTO.setOvertimeId(group.getOvertimeConfig() != null ? group.getOvertimeConfig().getId() : null);
        detailDTO.setName(group.getName());

        List<AttendanceGroupDTO.GroupShiftDTO> shiftList = buildGroupShiftList(group.getShiftList());
        detailDTO.setShifts(shiftList.stream().sorted(Comparator.comparing(AttendanceGroupDTO.GroupShiftDTO::getWeekday)).collect(Collectors.toList()));
        List<AttendanceGroupDetailDTO.GroupMemberDTO> members = buildMemberList(group.getId());
        detailDTO.setMembers(members);
        return detailDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object add(AttendanceGroupAddReq req) {
        if (Objects.equals(req.getType(), AttendanceGroupTypeEnum.FIXED.getCode()) && (req.getShiftList() == null || req.getShiftList().isEmpty())) {
            throw new BizException("", "请选择班次信息");
        }

        AttendanceGroup attendanceGroup = new AttendanceGroup()
                .setName(req.getName())
                .setHolidays(req.getHolidays())
                .setEmpCount(req.getEmpList().size())
                .setType(req.getType());
        if (req.getOvertimeId() != null) {
            WorkOvertimeConfig config = this.configRepository.findById(req.getOvertimeId()).orElseThrow(() -> new BizException("", "未找到加班配置"));
            attendanceGroup.setOvertimeConfig(config);
        }

        attendanceGroup.setEmpCount(req.getEmpList().size());

        attendanceGroup = groupRepository.save(attendanceGroup);

        List<AttendanceGroupMember> memberList = buildMemberList(attendanceGroup.getId(), req.getEmpList());
        groupMemberRepository.saveAll(memberList);

        List<AttendanceGroupShift> shiftList = buildShiftList(attendanceGroup.getId(), req.getShiftList());
        groupShiftRepository.saveAll(shiftList);

        return attendanceGroup;
    }

    private List<AttendanceGroupMember> buildMemberList(Long groupId, Set<Long> empIds) {
        return empIds.stream().map(item -> {
            AttendanceGroupMember member = new AttendanceGroupMember();
            FeignEmployeeDTO employeeDTO = feignEmployeeService.findById(item);
            if (null == employeeDTO) {
                throw new BizException("", "用户不存在");
            }

            AttendanceGroupMember existMember = groupMemberRepository.findByEmpId(item);
            if (null != existMember && existMember.getGroupId().intValue() != groupId.intValue()) {
                throw new BizException("", "用户[" + employeeDTO.getName() + "]已设置考勤组");
            }

            member.setEmpId(item);
            member.setGroupId(groupId);
            return member;

        }).collect(Collectors.toList());
    }

    private List<AttendanceGroupShift> buildShiftList(Long groupId, List<AttendanceGroupAddReq.GroupShiftReq> req) {
        List<AttendanceGroupShift> groupShifts = req.stream().map(item -> {
            AttendanceGroupShift shift = new AttendanceGroupShift();
            if (item.getShiftIds() != null) {
                AttendanceShift attendanceShift = shiftRepository.findById(item.getShiftIds()).orElseThrow(() -> new BizException("", "未找到班次组"));
                shift.setShifts(attendanceShift);
            }
            shift.setWeekday(item.getWeekday());
            shift.setGroupId(groupId);
            return shift;
        }).collect(Collectors.toList());
        return groupShifts;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object update(AttendanceGroupEditReq req) {
        AttendanceGroup attendanceGroup = groupRepository.findById(req.getId()).orElseThrow(() -> new BizException("record_not_exists", "记录不存在"));

        if (Objects.equals(req.getType(), AttendanceGroupTypeEnum.FIXED.getCode()) && (req.getShiftList() == null || req.getShiftList().isEmpty())) {
            throw new BizException("", "请选择班次信息");
        }

        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, attendanceGroup);

        if (req.getOvertimeId() != null) {
            WorkOvertimeConfig config = this.configRepository.findById(req.getOvertimeId()).orElseThrow(() -> new BizException("", "未找到加班配置"));
            attendanceGroup.setOvertimeConfig(config);
        } else {
            attendanceGroup.setOvertimeConfig(null);
        }


        groupMemberRepository.deleteByGroupId(attendanceGroup.getId());
        groupShiftRepository.deleteByGroupId(attendanceGroup.getId());

        attendanceGroup.setEmpCount(req.getEmpList().size());
        List<AttendanceGroupMember> memberList = buildMemberList(attendanceGroup.getId(), req.getEmpList());
        groupMemberRepository.saveAll(memberList);

        List<AttendanceGroupShift> shiftList = buildShiftList(attendanceGroup.getId(), req.getShiftList());
        groupShiftRepository.saveAll(shiftList);

        attendanceGroup.setMemberList(new HashSet<>(memberList));
        attendanceGroup.setShiftList(new HashSet<>(shiftList));
        groupRepository.save(attendanceGroup);

        return attendanceGroup;
    }

    /**
     * 排班
     */
    public void roster(List<AttendanceGroupSystemAddReq> reqs) {
        Long groupId = !CollectionUtils.isEmpty(reqs) ? reqs.get(0).getGroupId() : null;
        if (groupId != null) {
            AttendanceGroup group = this.groupRepository.findById(groupId).orElseThrow(() -> new BizException("", "未找到班次组"));
            if (!Objects.equals(group.getType(), AttendanceGroupTypeEnum.SHIFT_SYSTEM.getCode())) {
                throw new BizException("", "固定班制类型班组无法排班");
            }
        }
        for (AttendanceGroupSystemAddReq req : reqs) {
            AttendanceGroupShiftSystem shiftSystem = this.shiftSystemRepository.findByGroupAndEmp(req.getGroupId(), req.getEmployeeId());
            if (shiftSystem == null) {
                shiftSystem = new AttendanceGroupShiftSystem();
            }

            //人员信息
            FeignEmployeeDTO employeeDTO = feignEmployeeService.findById(req.getEmployeeId());
            if (null == employeeDTO) {
                throw new BizException("", "未找到人员信息");
            }

            shiftSystem.setGroupId(req.getGroupId())
                    .setEmpId(req.getEmployeeId());
            this.shiftSystemRepository.save(shiftSystem);

            for (AttendanceGroupSystemAddReq.SystemDate systemDate : req.getDateList()) {
                AttendanceGroupShiftSystemDate shiftSystemDate = new AttendanceGroupShiftSystemDate()
                        .setDate(systemDate.getDate())
                        .setShiftSystemId(shiftSystem.getId())
                        .setShift(systemDate.getShiftId() != null ? this.shiftRepository.findById(systemDate.getShiftId()).orElseThrow(() -> new BizException("", "不存在班次信息" + systemDate.getShiftId())) : null);
                this.shiftSystemDateRepository.save(shiftSystemDate);
            }
        }
    }

    /**
     * 查询班次信息
     *
     * @param id
     * @return
     */
    public List<AttendanceGroupSystemAddReq> rosterDetail(Long id, String dateMonth) {
        AttendanceGroup group = this.groupRepository.findById(id).orElseThrow(() -> new BizException("", "未找到数据"));
        if (!Objects.equals(group.getType(), AttendanceGroupTypeEnum.SHIFT_SYSTEM.getCode())) {
            throw new BizException("", "固定班制类型班组无法排班");
        }

        List<AttendanceGroupSystemAddReq> result = new ArrayList<>();

        FeignEmployeeDTO employeeDTO;
        for (AttendanceGroupMember attendanceGroupMember : group.getMemberList()) {
            if (attendanceGroupMember.getEmpId() == null) {
                continue;
            }
            AttendanceGroupShiftSystem item = group.getShiftSystems()
                    .stream()
                    .filter(shiftSystem -> shiftSystem.getEmpId() != null && Objects.equals(shiftSystem.getEmpId(), attendanceGroupMember.getEmpId()))
                    .findFirst()
                    .orElse(null);

            AttendanceGroupSystemAddReq dto = new AttendanceGroupSystemAddReq();
            dto.setGroupId(attendanceGroupMember.getGroupId());
            dto.setEmployeeId(attendanceGroupMember.getEmpId());

            //人员信息
            employeeDTO = feignEmployeeService.findById(attendanceGroupMember.getEmpId());
            if (null == employeeDTO) {
                throw new BizException("", "未找到人员信息");
            }
            dto.setEmployeeName(employeeDTO.getName());

            if (item != null) {
                List<AttendanceGroupSystemAddReq.SystemDate> systemDates = this.shiftSystemDateRepository.findByShiftIdAndDate(item.getId(), dateMonth).stream().map(date -> {
                    AttendanceGroupSystemAddReq.SystemDate systemDate = new AttendanceGroupSystemAddReq.SystemDate();
                    systemDate.setDate(date.getDate());
                    systemDate.setShiftId(date.getShift() != null ? date.getShift().getId() : null);
                    return systemDate;
                }).collect(Collectors.toList());
                dto.setDateList(systemDates);
            }
            //为空时添加默认值
            if (dto.getDateList() == null || dto.getDateList().isEmpty()) {
                dto.setDateList(this.defaultDate(dateMonth));
            }
            result.add(dto);
        }

        if (CollectionUtils.isEmpty(result)) {
            result = new ArrayList<>(group.getMemberList().size());
            for (AttendanceGroupMember item : group.getMemberList()) {
                if (item.getEmpId() != null) {
                    AttendanceGroupSystemAddReq dto = new AttendanceGroupSystemAddReq();
                    dto.setGroupId(item.getGroupId());
                    dto.setEmployeeId(item.getEmpId());
                    dto.setDateList(this.defaultDate(dateMonth));

                    //人员信息
                    employeeDTO = feignEmployeeService.findById(item.getEmpId());
                    if (null == employeeDTO) {
                        throw new BizException("未找到人员信息");
                    }
                    dto.setEmployeeName(employeeDTO.getName());
                    result.add(dto);
                }
            }
        }
        return result;
    }

    private List<AttendanceGroupSystemAddReq.SystemDate> defaultDate(String dateMonth) {
        List<AttendanceGroupSystemAddReq.SystemDate> systemDateList = new ArrayList<>();
        LocalDate nowdate = LocalDate.parse(dateMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(1);
        LocalDate lastDay = nowdate.with(TemporalAdjusters.lastDayOfMonth());
        for (int i = 1; i <= lastDay.getDayOfMonth(); i++) {
            LocalDate thistime = nowdate.withDayOfMonth(i);
            AttendanceGroupSystemAddReq.SystemDate systemDate = new AttendanceGroupSystemAddReq.SystemDate();
            systemDate.setDate(thistime);
            systemDateList.add(systemDate);
        }

        return systemDateList;
    }

    public List<HolidayDto> findDateByMonth(String month) {
        LocalDate nowdate = LocalDate.parse(month + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate lastDay = nowdate.with(TemporalAdjusters.lastDayOfMonth());
        List<HolidayDto> holidayDtos = new ArrayList<>(lastDay.getDayOfMonth());
        for (int i = 1; i <= lastDay.getDayOfMonth(); i++) {
            LocalDate thistime = nowdate.withDayOfMonth(i);

            HolidayDto holidayDto = new HolidayDto()
                    .setDate(thistime);

            holidayDto.setWeek(thistime.getDayOfWeek().getValue());
            holidayDto.setState(holidayDto.getWeek() == 6 || holidayDto.getWeek() == 7 ? 1 : 0);
            FeignHolidayDTO holiday = this.feignHolidayService.getByDate(thistime);
            if (null != holiday) {
                holidayDto.setName(holiday.getName());
                holidayDto.setState(holiday.getHoliday() ? 3 : 2);
            }

            holidayDtos.add(holidayDto);
        }
        return holidayDtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object delete(Long id) {
        Optional<AttendanceGroup> optional = groupRepository.findById(id);
        if (optional.isPresent()) {
            groupMemberRepository.deleteByGroupId(id);
            groupShiftRepository.deleteByGroupId(id);
            shiftSystemRepository.deleteByGroupId(id);
            groupRepository.deleteById(id);
        }

        return optional.get();
    }


    @Transactional(rollbackFor = Exception.class)
    public Object batchDelete(Long[] ids) {
        List<AttendanceGroup> groupList = new ArrayList<>();
        AttendanceGroup group;
        for (Long id : ids) {
            group = groupRepository.findById(id).orElseThrow(() -> new BizException("", "删除项不存在"));
            groupList.add(group);

            groupMemberRepository.deleteByGroupId(id);
            groupShiftRepository.deleteByGroupId(id);
            shiftSystemRepository.deleteByGroupId(id);
            groupRepository.deleteById(id);
        }
        return groupList;
    }
}