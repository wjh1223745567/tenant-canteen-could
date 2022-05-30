package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.overtime.WorkOvertimeRecordCondition;
import com.iotinall.canteen.dto.overtime.WorkOvertimeRecordDto;
import com.iotinall.canteen.service.WorkOvertimeRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 加班记录
 */
@Api(tags = "加班记录 权限：加班记录 WORK_OVERTIME_RECORD_ADE")
@RestController
@RequestMapping(value = "work_overtime_record")
public class WorkOvertimeRecordController {

    @Resource
    private WorkOvertimeRecordService recordService;

    public static final String modelKey = "WORK_OVERTIME_RECORD";

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL', '" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "加班记录列表", response = WorkOvertimeRecordDto.class)
    public ResultDTO findPage(WorkOvertimeRecordCondition condition, Pageable pageable){
        return ResultDTO.success(this.recordService.findPage(condition, pageable));
    }

}
