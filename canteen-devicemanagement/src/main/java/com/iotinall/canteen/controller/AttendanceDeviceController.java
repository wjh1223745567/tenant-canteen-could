package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.attendance.EquipmentAddReq;
import com.iotinall.canteen.dto.attendance.EquipmentDTO;
import com.iotinall.canteen.dto.attendance.EquipmentEmployeeRelationDTO;
import com.iotinall.canteen.dto.attendance.EquipmentQryReq;
import com.iotinall.canteen.service.AttendanceDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/device/attendance")
@Api(tags = SwaggerModule.MODULE_FACE_RECOGNITION)
public class AttendanceDeviceController {

    @Resource
    private AttendanceDeviceService equipmentService;

    @ApiOperation(value = "人脸设备列表", notes = "人脸设备列表", httpMethod = "GET", response = EquipmentDTO.class)
    @GetMapping("/all")
    public ResultDTO list(@ModelAttribute EquipmentQryReq req) {
        return ResultDTO.success(equipmentService.list(req));
    }

    @ApiOperation(value = "人脸设备分页", notes = "人脸设备分页", httpMethod = "GET", response = EquipmentDTO.class)
    @GetMapping
    public ResultDTO page(@ModelAttribute EquipmentQryReq req,
                          @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(equipmentService.page(req, pageable));
    }

    @ApiOperation(value = "新增考勤设备", notes = "新增考勤设备", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('CANTEEN_DEVICE','DEVICE_ATTENDANCE_EDIT')")
    public ResultDTO create(@Valid @RequestBody EquipmentAddReq req) {
        equipmentService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "修改考勤设备", notes = "修改考勤设备", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('CANTEEN_DEVICE','DEVICE_ATTENDANCE_EDIT')")
    public ResultDTO update(@Valid @RequestBody EquipmentAddReq req) {
        equipmentService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "批量删除考勤设备", notes = "批量删除考勤设备", httpMethod = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('CANTEEN_DEVICE','DEVICE_ATTENDANCE_EDIT')")
    public ResultDTO batchDelete(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Set<Long> ids) {
        equipmentService.batchDelete(ids);
        return ResultDTO.success();
    }

    @ApiOperation(value = "同步人员", notes = "同步人员", httpMethod = "POST")
    @PostMapping("/{id}")
    public ResultDTO syncOrgEmployeeToEquipment(@PathVariable(value = "id") Long id) {
        equipmentService.syncOrgEmployeeToEquipment(id);
        return ResultDTO.success();
    }

    @ApiOperation(value = "批量同步人员", notes = "批量同步人员", httpMethod = "POST")
    @PostMapping("sync")
    public ResultDTO syncBatchOrgEmployeeToEquipment(@ApiParam(name = "equIds", value = "需要同步的设备id，逗号分隔", required = true) @RequestParam(value = "equIds") Set<Long> equIds,
                                                     @ApiParam(name = "empIds", value = "需要同步的人员id，逗号分隔", required = true) @RequestParam(value = "empIds") Set<Long> empIds) {
        equipmentService.syncBatchOrgEmployeeToEquipment(equIds, empIds);
        return ResultDTO.success();
    }

    @ApiOperation(value = "设备人员关系列表", notes = "设备人员关系列表", httpMethod = "GET", response = EquipmentEmployeeRelationDTO.class)
    @GetMapping("/equ&emp/all")
    public ResultDTO equEmpList() {
        return ResultDTO.success(equipmentService.equEmpList());
    }


}
