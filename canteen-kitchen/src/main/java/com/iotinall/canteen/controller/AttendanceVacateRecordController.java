package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.attendancevacaterecord.*;
import com.iotinall.canteen.service.AttendanceVacateRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 请假记录
 *
 * @author xinbing
 * @date 2020-07-14 16:29:49
 */
@Api(tags = "请假记录")
@RestController
@RequestMapping("attendance-vacate-records")
public class AttendanceVacateRecordController {

    @Resource
    private AttendanceVacateRecordService attendanceVacateRecordService;

    @ApiOperation(value = "查询请假记录", notes = "条件查询请假记录列表")
    @GetMapping
    public ResultDTO<PageDTO<AttendanceVacateRecordDTO>> list(AttendanceVacateRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC)Pageable pageable) {
        return ResultDTO.success(attendanceVacateRecordService.list(criteria, pageable));
    }

    @ApiOperation(value = "添加请假记录", notes = "修改请假记录")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_VACATE','APP_HCGL','APP_HCGL_YGKQ','APP_HCGL_YGKQ_QJ')")
    public ResultDTO<?> create(@Valid @RequestBody AttendanceVacateRecordAddReq req) {
        attendanceVacateRecordService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "修改请假记录", notes = "修改请假记录")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_VACATE','APP_HCGL','APP_HCGL_YGKQ','APP_HCGL_YGKQ_QJ')")
    public ResultDTO<?> update(@Valid @RequestBody AttendanceVacateRecordEditReq req) {
        attendanceVacateRecordService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "请假记录审批", notes = "请假记录审批")
    @PostMapping("audit")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_VACATE_APPLY')")
    public ResultDTO<?> audit(@Valid @RequestBody AttendanceVacateRecordAuditReq req) {
        attendanceVacateRecordService.audit(req);
        return ResultDTO.success();
    }

    /**
     * 删除请假记录
     *
     * @author xinbing
     * @date 2020-07-14 16:29:49
     */
    @ApiOperation(value = "删除请假记录", notes = "修改请假记录")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_VACATE')")
    public ResultDTO<?> delete(@ApiParam(name = "id", value = "主键", required = true)
                               @PathVariable Long id) {
        attendanceVacateRecordService.delete(id);
        return ResultDTO.success();
    }

    /**
     * 批量删除请假记录
     *
     * @author xinbing
     * @date 2020-07-14 16:29:49
     */
    @ApiOperation(value = "批量删除请假记录")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_VACATE')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        attendanceVacateRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}