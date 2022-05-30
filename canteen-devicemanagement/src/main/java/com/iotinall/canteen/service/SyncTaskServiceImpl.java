package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSONObject;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.constants.PersonalType;
import com.iotinall.canteen.constants.RedisConstants;
import com.iotinall.canteen.constants.TerminalConstants;
import com.iotinall.canteen.dto.attendance.face.*;
import com.iotinall.canteen.entity.DeviceAttendance;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.repository.AttendanceDeviceRepository;
import com.iotinall.canteen.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 设备数据同步任务处理类
 **/
@Slf4j
@Component
public class SyncTaskServiceImpl implements SyncTaskService {
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FaceTerminalFullHttpRequestServiceImpl faceTerminalFullHttpRequestService;
    @Resource
    private AttendanceDeviceRepository equipmentRepository;

    @Override
    public void syncToTerminal(Integer type) {
        this.syncOrgEmployeeToTerminal(false);
    }

    @Override
    public void syncToTerminal(Integer optType, Long empId) {
        //人脸终端设备
        List<DeviceAttendance> equipmentList = this.equipmentRepository.findByType(TerminalConstants.EQU_TYPE_FACE_RECOGNITION);
        if (CollectionUtils.isEmpty(equipmentList)) {
            return;
        }

        FeignSimEmployeeDto employee;
        for (DeviceAttendance terminal : equipmentList) {
            if (terminal.getStatus() != TerminalConstants.EQU_STATUS_NORMAL) {
                continue;
            }

            if (optType == RedisConstants.SYNC_TYPE_DEL) {
                FaceTerminalQryResponse result = this.faceTerminalFullHttpRequestService.delFromTerminal(terminal.getDeviceNo(), terminal.getEmpLib(), empId);
                log.info("请求结果:{}", JSONObject.toJSONString(result));
            } else {
                employee = this.feignEmployeeService.findSimById(empId);
                if (null == employee) {
                    return;
                }
                this.syncOrgEmployee(terminal, employee);
            }
        }
    }

    @Override
    public void fullSyncToTerminal(Integer type) {
        this.syncOrgEmployeeToTerminal(true);
    }

    /**
     * 同步后厨人员到终端
     */
    @Override
    public void syncOrgEmployeeToTerminal(Boolean full) {
        //人脸终端设备
        List<DeviceAttendance> equipmentList = this.equipmentRepository.findByType(TerminalConstants.EQU_TYPE_FACE_RECOGNITION);
        if (CollectionUtils.isEmpty(equipmentList)) {
            return;
        }

        for (DeviceAttendance terminal : equipmentList) {
            if (terminal.getStatus() != TerminalConstants.EQU_STATUS_NORMAL) {
                if (!full) {
                    continue;
                }

                //生成同步记录
                log.info("生成同步记录");
            }
            new Thread(() -> this.syncOrgEmployee(terminal)).start();
        }
    }

    /**
     * 同步后厨人员到设备
     */
    private void syncOrgEmployee(DeviceAttendance terminal) {
        //所有的后厨人员
        List<FeignSimEmployeeDto> employeeList = feignEmployeeService.findAllByType(PersonalType.BACKKITCHEN.getCode(), Boolean.FALSE);
        if (CollectionUtils.isEmpty(employeeList)) {
            return;
        }

        for (FeignSimEmployeeDto employee : employeeList) {
            try {
                this.syncOrgEmployee(terminal, employee);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void syncOrgEmployee(DeviceAttendance terminal, FeignSimEmployeeDto employee) {
        if (StringUtils.isBlank(employee.getImg())) {
            return;
        }

        Long lib = terminal.getEmpLib();

        FaceTerminalQryResponse result = faceTerminalFullHttpRequestService.getPersonFromTerminal(terminal.getDeviceNo(), terminal.getEmpLib(), String.valueOf(employee.getId()));

        //判断员工是否存在于设备中
        FaceTerminalPersonDetail person;
        FaceTerminalAddResultResponse response;
        if (null != result) {
            if (result.getResponse().getData().getTotal() <= 0) {
                //添加
                person = this.buildPersonDetail(employee);
                response = this.faceTerminalFullHttpRequestService.addToTerminal(terminal.getDeviceNo(), this.buildUpdatePersonDetail(person, employee), lib);
            } else {
                //更新
                person = this.buildUpdatePersonDetail(result.getResponse().getData().getPersonList().getPersonInfoList().get(0), employee);
                response = this.faceTerminalFullHttpRequestService.updateToTerminal(terminal.getDeviceNo(), person, lib);
            }
            log.info("请求结果:{}", JSONObject.toJSONString(response));
        }
    }

    /**
     * 构建personDetail
     */
    private FaceTerminalPersonDetail buildUpdatePersonDetail(FaceTerminalPersonDetail detail, FeignSimEmployeeDto employee) {
        detail.setPersonCode(employee.getId() + "");
        detail.setPersonName(employee.getName());
        detail.setRemarks("备注");

        if (StringUtils.isNotBlank(employee.getIdNo())) {
            detail.setIdentificationList(this.buildIdentification(employee.getIdNo()));
            detail.setIdentificationNum(null == detail.getIdentificationList() ? 0 : detail.getIdentificationList().size());
        } else {
            detail.setIdentificationNum(0);
        }

        detail.setImageList(this.buildPersonImageList(employee));
        return detail;
    }

    /**
     * 构建personDetail
     */
    private FaceTerminalPersonDetail buildPersonDetail(FeignSimEmployeeDto employee) {
        FaceTerminalPersonDetail person = new FaceTerminalPersonDetail();
        person.setPersonId(employee.getId());
        person.setPersonCode(employee.getId() + "");
        person.setPersonName(employee.getName());

        if (StringUtils.isNotBlank(employee.getIdNo())) {
            person.setIdentificationList(this.buildIdentification(employee.getIdNo()));
            person.setIdentificationNum(person.getIdentificationList().size());
        } else {
            person.setIdentificationNum(0);
        }

        person.setImageList(this.buildPersonImageList(employee));
        person.setImageNum(person.getImageList().size());
        person.setTimeTemplateNum(0);
        person.setRemarks("备注");
        person.setLastChange(System.currentTimeMillis());
        return person;
    }

    /**
     * 构建时间模板
     *
     * @author loki
     * @date 2020/7/2 14:21
     */
    private List<FaceTerminalPersonTimeTemplate> buildTimeTemplate(Long index) {
        FaceTerminalPersonTimeTemplate timeTemplate = new FaceTerminalPersonTimeTemplate();
        timeTemplate.setBeginTime(0L);
        timeTemplate.setEndTime(4294967295L);
        timeTemplate.setIndex(index);
        return Collections.singletonList(timeTemplate);
    }

    /**
     * 构建头像信息
     *
     * @author loki
     * @date 2020/06/15 15:33
     */
    private List<FaceTerminalPersonImage> buildPersonImageList(FeignSimEmployeeDto employee) {
        List<FaceTerminalPersonImage> imageList = new ArrayList<>();
        FaceTerminalPersonImage image = new FaceTerminalPersonImage();
        image.setFaceId(employee.getId());
        image.setName(employee.getId() + ".jpg");
        String base64 = FileUtil.getBase64FromUrl(ImgPair.getFileServer() + employee.getImg());
        image.setData(base64);
        image.setSize(image.getData().length());
        imageList.add(image);
        return imageList;
    }

    /**
     * 构建证件信息
     *
     * @author loki
     * @date 2020/06/15 15:25
     */
    private List<FaceTerminalPersonIdentification> buildIdentification(String identityNo) {
        List<FaceTerminalPersonIdentification> identificationList = new ArrayList<>();
        FaceTerminalPersonIdentification identification = new FaceTerminalPersonIdentification();
        identification.setType(TerminalConstants.IDENTIFICATION_TYPE_ID_NUMBER);
        identification.setNumber(identityNo);
        identificationList.add(identification);

        return identificationList;
    }
}
