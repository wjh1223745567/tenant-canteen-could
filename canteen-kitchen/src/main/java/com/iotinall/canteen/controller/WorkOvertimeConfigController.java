package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.overtime.WorkOvertimeConfigAddReq;
import com.iotinall.canteen.dto.overtime.WorkOvertimeConfigCondition;
import com.iotinall.canteen.dto.overtime.WorkOvertimeConfigDto;
import com.iotinall.canteen.service.WorkOvertimeConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "加班配置接口 权限：加班配置 WORK_OVERTIME_CONFIG_ADE ")
@RestController
@RequestMapping(value = "work_overtime_config")
public class WorkOvertimeConfigController {

    @Resource
    private WorkOvertimeConfigService configService;

    public static final String modelKey = "WORK_OVERTIME_CONFIG";

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS', '" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "配置列表查询", notes = "配置列表", httpMethod = "GET", response = WorkOvertimeConfigDto.class)
    public ResultDTO<PageDTO<WorkOvertimeConfigDto>> findPage(WorkOvertimeConfigCondition condition, Pageable pageable) {
        return ResultDTO.success(this.configService.findPage(condition, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL', 'KITCHEN_ATTENDANCE_SETTINGS','" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "添加配置", notes = "添加配置", httpMethod = "POST")
    public ResultDTO add(@Valid @RequestBody WorkOvertimeConfigAddReq req) {
        this.configService.add(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS', '" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "编辑配置", notes = "编辑配置", httpMethod = "PUT")
    public ResultDTO edit(@Valid @RequestBody WorkOvertimeConfigAddReq req) {
        this.configService.edit(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL', 'KITCHEN_ATTENDANCE_SETTINGS','" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "批量删除配置信息")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        configService.batchDelete(ids);
        return ResultDTO.success();
    }

}
