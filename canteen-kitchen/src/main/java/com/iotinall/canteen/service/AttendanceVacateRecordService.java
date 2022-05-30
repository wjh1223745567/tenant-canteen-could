package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.attendancevacaterecord.*;
import com.iotinall.canteen.entity.AttendanceVacateRecord;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.AttendanceVacateRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 请假记录 ServiceImpl
 *
 * @author xinbing
 * @date 2020-07-14 16:29:49
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class AttendanceVacateRecordService {
    @Resource
    private AttendanceVacateRecordRepository attendanceVacateRecordRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    public PageDTO<AttendanceVacateRecordDTO> list(AttendanceVacateRecordCriteria criteria, Pageable pageable) {
        List<Criterion> criterions = new ArrayList<>();
        if (SecurityUtils.getCurrentUser().getPermissions().stream().noneMatch(item -> ArrayUtils.contains(new String[]{"ADMIN", "KITCHEN_ALL", "KITCHEN_VACATE_APPLY"}, item))) {
            criterions.add(Criterion.eq("employee.id", SecurityUtils.getUserId()));
        }
        Specification<AttendanceVacateRecord> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("state", criteria.getState()))
                .where(Criterion.gte("beginTime", criteria.getBeginTime() == null ? null : criteria.getBeginTime().atTime(LocalTime.MIN)))
                .where(Criterion.lt("endTime", criteria.getEndTime() == null ? null : criteria.getEndTime().atTime(LocalTime.MAX)))
                .where(Criterion.like("empName", criteria.getKeyword()))
                .where(criterions.toArray(new Criterion[0]))
                .build();

        Page<AttendanceVacateRecord> page = attendanceVacateRecordRepository.findAll(spec, pageable);
        List<AttendanceVacateRecordDTO> list = page.getContent().stream().map(item -> {
            AttendanceVacateRecordDTO attendanceVacateRecordDTO = new AttendanceVacateRecordDTO();
            // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
            BeanUtils.copyProperties(item, attendanceVacateRecordDTO);

            if (null != item.getEmpId()) {
                FeignEmployeeDTO employeeDTO = this.feignEmployeeService.findById(item.getEmpId());
                attendanceVacateRecordDTO.setUsed(Objects.equals(item.getEmpId(), SecurityUtils.getUserId()));
                attendanceVacateRecordDTO.setEmpId(employeeDTO.getId());
                attendanceVacateRecordDTO.setEmpName(employeeDTO.getName());
                attendanceVacateRecordDTO.setAvatar(employeeDTO.getAvatar());
                attendanceVacateRecordDTO.setRole(employeeDTO.getPosition());
            }
            return attendanceVacateRecordDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(@Valid @RequestBody AttendanceVacateRecordAddReq req) {
        AttendanceVacateRecord record = new AttendanceVacateRecord();
        BeanUtils.copyProperties(req, record);
        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setState(0);

        FeignEmployeeDTO employeeDTO = this.feignEmployeeService.findById(SecurityUtils.getUserId());
        record.setEmpId(employeeDTO.getId());
        record.setEmpName(employeeDTO.getName());
        attendanceVacateRecordRepository.save(record);

        //找到具有审核请假权限的用户，发送消息
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
//                            if (Objects.equals(sysPermission.getPermission(), "KITCHEN_VACATE_APPLY")) {
//                                log.info("审核人:" + orgEmployee.getName());
//                                log.info("申请人：" + applyEmployee.getName());
//                                log.info("所属部门：" + applyEmployee.getRole());
//                                log.info("审核内容：" + record.getReason());
//                                log.info("审核状态：" + "待审核");
//                                this.wxMessageSendService.sendFeedbackNotice(orgEmployee.getOpenid(), applyEmployee.getName(), applyEmployee.getRole(), record.getReason(), "待审核", MiniProgramPageEnum.KITCHEN_MANAGEMENT_APPLICANT_MENU.getValue());
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(AttendanceVacateRecordEditReq req) {
        Optional<AttendanceVacateRecord> optional = attendanceVacateRecordRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        AttendanceVacateRecord record = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        LocalDateTime now = LocalDateTime.now();
        record.setAuditTime(now);
        attendanceVacateRecordRepository.save(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public void audit(AttendanceVacateRecordAuditReq req) {
        Optional<AttendanceVacateRecord> optional = attendanceVacateRecordRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
//        OrgEmployee attEmp = optional.get().getEmployee();
//        if (attEmp == null) {
//            throw new BizException("", "未找到申请人");
//        }
//        OrgEmployee employee = this.employeeRepository.findById(SecurityUtils.getUserId()).orElseThrow(() -> new BizException("", "未找到当前用户"));
        AttendanceVacateRecord record = optional.get();
        record.setAuditOpinion(req.getAuditOpinion());
        record.setAuditorId(SecurityUtils.getUserId());
        record.setAuditTime(LocalDateTime.now());
        record.setState(req.getState());
        attendanceVacateRecordRepository.save(record);
        String state = null;
        int flag = record.getState();
        if (flag == 1) {
            state = "审批通过";
        }
        if (flag == 2) {
            state = "审批拒绝";
        }
//        log.info("申请人：" + attEmp.getName());
//        log.info("所属部门：" + attEmp.getRole());
//        log.info("审核内容：" + optional.get().getReason());
//        log.info("审核状态：" + state);
//        this.wxMessageSendService.sendkitchenManagementApplicantMessage(attEmp.getOpenid(), attEmp.getName(), attEmp.getRole(), optional.get().getReason(), state, MiniProgramPageEnum.KITCHEN_MANAGEMENT_APPLICANT_MENU.getValue());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.attendanceVacateRecordRepository.deleteById(id);
    }

    /**
     * 计算请假时长
     *
     * @param empId
     * @param beginTime
     * @param endTime
     * @return
     */
    public Long timeOff(Long empId, LocalDateTime beginTime, LocalDateTime endTime) {
        List<AttendanceVacateRecord> vacateRecords = this.attendanceVacateRecordRepository.findBySuccessTime(empId, beginTime, endTime);
        return vacateRecords.stream().mapToLong(item -> Math.abs(Duration.between(item.getBeginTime(), item.getEndTime()).toHours())).sum();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        attendanceVacateRecordRepository.deleteByIdIn(ids);
    }
}