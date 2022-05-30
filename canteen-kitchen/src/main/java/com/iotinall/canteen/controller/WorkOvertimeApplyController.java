package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.overtime.WorkOvertimeApplyAddReq;
import com.iotinall.canteen.dto.overtime.WorkOvertimeApplyCondition;
import com.iotinall.canteen.dto.overtime.WorkOvertimeApplyDto;
import com.iotinall.canteen.dto.overtime.WorkOvertimeApplyReq;
import com.iotinall.canteen.service.WorkOvertimeApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "加班申请管理 权限：加班申请 WORK_OVERTIME_APPLY_ADE 加班审核 WORK_OVERTIME_APPLY_APPLY")
@RestController
@RequestMapping(value = "work_overtime_apply")
public class WorkOvertimeApplyController {

    @Resource
    private WorkOvertimeApplyService applyService;

    public static final String modelKey = "WORK_OVERTIME_APPLY";

    /**
     * 分页
     *
     * @param condition
     * @param pageable
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL','WORK_OVERTIME_APPLY_APPLY', '" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "加班申请列表", notes = "加班申请列表", httpMethod = "GET", response = WorkOvertimeApplyDto.class)
    public ResultDTO<PageDTO<WorkOvertimeApplyDto>> findByPage(WorkOvertimeApplyCondition condition, Pageable pageable) {
        return ResultDTO.success(this.applyService.findPage(condition, pageable));
    }

    /**
     * 申请加班
     *
     * @param req
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL', 'WORK_OVERTIME_APPLY_APPLY','" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "添加申请", notes = "添加申请", httpMethod = "POST")
    public ResultDTO<?> add(@Valid @RequestBody WorkOvertimeApplyAddReq req) {
        this.applyService.add(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL', 'WORK_OVERTIME_APPLY_APPLY','" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "编辑申请", tags = "编辑申请", httpMethod = "PUT")
    public ResultDTO<?> edit(@Valid @RequestBody WorkOvertimeApplyAddReq req) {
        this.applyService.edit(req);
        return ResultDTO.success();
    }

    @GetMapping(value = "cancel/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL', 'WORK_OVERTIME_APPLY_APPLY','" + modelKey + "_ALL', '" + modelKey + "_ADE')")
    @ApiOperation(value = "取消申请", tags = "取消申请", httpMethod = "GET")
    public ResultDTO cancel(@PathVariable(name = "id") Long id) {
        this.applyService.cancel(id);
        return ResultDTO.success();
    }

    /**
     * 审核
     *
     * @param req
     * @return
     */
    @PostMapping(value = "apply")
    @PreAuthorize("hasAnyRole('ADMIN', 'KITCHEN_ALL', 'WORK_OVERTIME_APPLY_APPLY','" + modelKey + "_ALL', '" + modelKey + "_APPLY')")
    @ApiOperation(value = "审核申请", tags = "审核申请", httpMethod = "POST")
    public ResultDTO apply(@Valid @RequestBody WorkOvertimeApplyReq req) {
        this.applyService.apply(req);
        return ResultDTO.success();
    }

}
