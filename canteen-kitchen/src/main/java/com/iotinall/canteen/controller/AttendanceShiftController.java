package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftAddReq;
import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftCriteria;
import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftDTO;
import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftEditReq;
import com.iotinall.canteen.service.AttendanceShiftService;
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
 * 考勤班次
 *
 * @author xinbing
 * @date 2020-07-13 19:54:53
 */
@RestController
@RequestMapping("kitchen/attendance-shifts")
public class AttendanceShiftController {

    @Resource
    private AttendanceShiftService attendanceShiftService;

    @ApiOperation(value = "查询考勤班次", notes = "条件查询考勤班次列表")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<PageDTO<AttendanceShiftDTO>> list(AttendanceShiftCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(attendanceShiftService.list(criteria, pageable));
    }

    @ApiOperation(value = "新增考勤班次", notes = "新增考勤班次")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> add(@Valid @RequestBody AttendanceShiftAddReq req) {
        return ResultDTO.success(attendanceShiftService.add(req));
    }


    @ApiOperation(value = "修改考勤班次", notes = "修改考勤班次")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> update(@Valid @RequestBody AttendanceShiftEditReq req) {
        return ResultDTO.success(attendanceShiftService.update(req));
    }

    /**
     * 删除考勤班次
     *
     * @author xinbing
     * @date 2020-07-13 19:54:53
     */
    @ApiOperation(value = "删除考勤班次", notes = "修改考勤班次")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> delete(@ApiParam(name = "id", value = "主键", required = true)
                               @PathVariable Long id) {
        return ResultDTO.success(attendanceShiftService.delete(id));
    }

    /**
     * 批量删除考勤班次
     *
     * @author xinbing
     * @date 2020-07-13 19:54:53
     */
    @ApiOperation(value = "批量删除考勤班次")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        return ResultDTO.success(attendanceShiftService.batchDelete(ids));
    }
}