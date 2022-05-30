package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constants.TerminalConstants;
import com.iotinall.canteen.dto.attendance.EquipmentAddReq;
import com.iotinall.canteen.dto.attendance.EquipmentDTO;
import com.iotinall.canteen.dto.attendance.EquipmentEmployeeRelationDTO;
import com.iotinall.canteen.dto.attendance.EquipmentQryReq;
import com.iotinall.canteen.dto.attendance.face.FaceTerminalQryResponse;
import com.iotinall.canteen.entity.DeviceAttendance;
import com.iotinall.canteen.entity.EquipmentEmployeeRelation;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.AttendanceDeviceRepository;
import com.iotinall.canteen.repository.EquipmentEmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备服务
 *
 * @author loki
 * @date 2021/01/09 15:57
 */
@Slf4j
@Service
public class AttendanceDeviceService {
    @Resource
    private AttendanceDeviceRepository attendanceDeviceRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private SyncTaskServiceImpl syncTaskService;
    @Resource
    private EquipmentEmployeeRepository equipmentEmployeeRepository;
    @Resource
    private FaceTerminalFullHttpRequestServiceImpl faceTerminalFullHttpRequestService;


    /**
     * 更新设备心跳
     *
     * @author loki
     * @date 2021/01/09 15:58
     */
    public void updateEquipmentStatus(String code, String ip) {
        DeviceAttendance equipment = attendanceDeviceRepository.findByDeviceNo(code);
        if (null == equipment) {
            return;
        }
        equipment.setIp(ip);
        equipment.setStatus(TerminalConstants.EQU_STATUS_NORMAL);
        this.attendanceDeviceRepository.save(equipment);
    }

    public Object list(EquipmentQryReq req) {
        List<DeviceAttendance> result = this.attendanceDeviceRepository.findAll();
        if (CollectionUtils.isEmpty(result)) {
            return Collections.EMPTY_LIST;
        }
        return result.stream().map(this::convert).collect(Collectors.toList());
    }

    public Object page(EquipmentQryReq req, Pageable page) {
        Specification<DeviceAttendance> spec = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("name", req.getKeyword()),
                        Criterion.like("deviceNo", req.getKeyword()))
                .build();

        Page<DeviceAttendance> result = this.attendanceDeviceRepository.findAll(spec, page);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }
        return PageUtil.toPageDTO(result.stream().map(this::convert).collect(Collectors.toList()), result);
    }

    private EquipmentDTO convert(DeviceAttendance equipment) {
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        BeanUtils.copyProperties(equipment, equipmentDTO);
        return equipmentDTO;
    }

    public void create(EquipmentAddReq req) {
        DeviceAttendance equipment = this.attendanceDeviceRepository.findByDeviceNo(req.getDeviceNo());
        if (null != equipment) {
            throw new BizException("设备编号已存在");
        }
        equipment = new DeviceAttendance();
        BeanUtils.copyProperties(req, equipment);
        equipment.setIp(req.getIp());
        equipment.setStatus(TerminalConstants.STATUS.STATUS_INACTIVATED);
        equipment.setEmpLib(req.getEmpLib());
        equipment.setTenantOrgId(req.getTenantOrgId());
        equipment.setAreaName(req.getAreaName());

        this.attendanceDeviceRepository.save(equipment);
    }

    public void update(EquipmentAddReq req) {
        DeviceAttendance equipment = this.attendanceDeviceRepository.findById(req.getId()).orElseThrow(() -> new BizException("设备不存在"));

        BeanUtils.copyProperties(req, equipment);
        equipment.setStatus(TerminalConstants.STATUS.STATUS_INACTIVATED);
        equipment.setEmpLib(req.getEmpLib());
        equipment.setTenantOrgId(req.getTenantOrgId());
        equipment.setAreaName(req.getAreaName());
        attendanceDeviceRepository.save(equipment);
    }

    public void batchDelete(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BizException("请选择要删除的设备");
        }
        DeviceAttendance equipment;
        for (Long id : ids) {
            equipment = this.attendanceDeviceRepository.findById(id).orElseThrow(() -> new BizException("设备不存在"));
            this.attendanceDeviceRepository.delete(equipment);
        }
    }


    public Object equEmpList() {
        List<EquipmentEmployeeRelation> equipmentEmployeeRelationList = this.equipmentEmployeeRepository.findAll();
        if (CollectionUtils.isEmpty(equipmentEmployeeRelationList)) {
            return Collections.EMPTY_LIST;
        }
        return equipmentEmployeeRelationList.stream().map((equipmentEmployeeRelation) -> {
                    EquipmentEmployeeRelationDTO equipmentEmployeeRelationDTO = new EquipmentEmployeeRelationDTO();
                    BeanUtils.copyProperties(equipmentEmployeeRelation, equipmentEmployeeRelationDTO);
                    return equipmentEmployeeRelationDTO;
                }
        ).collect(Collectors.toList());
    }

    public void syncOrgEmployeeToEquipment(Long id) {
        DeviceAttendance equipment = this.attendanceDeviceRepository.findById(id).orElseThrow(() -> new BizException("设备不存在"));
        List<FeignSimEmployeeDto> employeeList = feignEmployeeService.findSimAll();
        if (CollectionUtils.isEmpty(employeeList)) {
            return;
        }
        for (FeignSimEmployeeDto orgEmployee : employeeList) {
            try {
                syncTaskService.syncOrgEmployee(equipment, orgEmployee);
                syncRecord(equipment, orgEmployee);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void syncBatchOrgEmployeeToEquipment(Set<Long> equIds, Set<Long> empIds) {
        for (Long equId : equIds) {
            DeviceAttendance equipment = this.attendanceDeviceRepository.findById(equId).orElseThrow(() -> new BizException("设备不存在"));
            for (Long empId : empIds) {
                FeignSimEmployeeDto orgEmployee = this.feignEmployeeService.findSimById(empId);
                try {
                    syncTaskService.syncOrgEmployee(equipment, orgEmployee);
                    syncRecord(equipment, orgEmployee);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新设备人员关系记录
     *
     * @param equipment
     * @param employee
     */
    public void syncRecord(DeviceAttendance equipment, FeignSimEmployeeDto employee) {
        EquipmentEmployeeRelation relation = this.equipmentEmployeeRepository.findByEquIdAndAndEmpId(equipment.getId(), employee.getId());
        if (relation == null) {
            throw new BizException("设备人员关系不存在");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        if (StringUtils.isBlank(employee.getImg())) {
            relation.setSyncStatus(2);
            this.equipmentEmployeeRepository.save(relation);
            relation.setUpdateTime(localDateTime);
            return;
        }
        FaceTerminalQryResponse qryResponse = faceTerminalFullHttpRequestService.getPersonFromTerminal(equipment.getDeviceNo(), equipment.getEmpLib(), employee.getIdNo());
        if (null != qryResponse) {
            if (qryResponse.getResponse().getData().getTotal() <= 0) {
                //未同步
                relation.setSyncStatus(0);
                relation.setUpdateTime(localDateTime);
            } else {
                //已同步
                relation.setSyncStatus(1);
                relation.setSyncTime(localDateTime);
                relation.setUpdateTime(localDateTime);
            }
            this.equipmentEmployeeRepository.save(relation);
        }
    }
}
