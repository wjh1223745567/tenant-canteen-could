package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.attendancegroup.*;
import com.iotinall.canteen.dto.attendanceroster.AttendanceGroupSystemAddReq;
import com.iotinall.canteen.dto.holiday.HolidayDto;
import com.iotinall.canteen.service.AttendanceGroupService;
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
import java.util.List;

/**
 * 考勤组
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@RestController
@RequestMapping("kitchen/attendance-groups")
public class AttendanceGroupController {

    @Resource
    private AttendanceGroupService attendanceGroupService;

    @ApiOperation(value = "查询考勤组", notes = "条件查询考勤组列表")
    @GetMapping
    public ResultDTO<PageDTO<AttendanceGroupDTO>> list(AttendanceGroupCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(attendanceGroupService.list(criteria, pageable));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取考勤组详情", notes = "考勤组详情")
    public ResultDTO<AttendanceGroupDetailDTO> details(@PathVariable("id") Long id) {
        return ResultDTO.success(attendanceGroupService.details(id));
    }

    @ApiOperation(value = "新增考勤组", notes = "新增考勤组")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> add(@Valid @RequestBody AttendanceGroupAddReq req) {
        return ResultDTO.success(attendanceGroupService.add(req));
    }


    @ApiOperation(value = "修改考勤组", notes = "修改考勤组")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> update(@Valid @RequestBody AttendanceGroupEditReq req) {
        return ResultDTO.success(attendanceGroupService.update(req));
    }

    @ApiOperation(value = "考勤组排班", notes = "考勤组排班")
    @PostMapping(value = "roster")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> roster(@Valid @RequestBody List<AttendanceGroupSystemAddReq> reqs) {
        attendanceGroupService.roster(reqs);
        return ResultDTO.success();
    }

    @ApiOperation(value = "查询已编排考勤组", notes = "查询已编排考勤组", response = AttendanceGroupSystemAddReq.class)
    @GetMapping(value = "roster_detail")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> getRosterDetail(@ApiParam(name = "考勤组ID") @RequestParam("id") Long id, @ApiParam(name = "排班日期yyyy-MM") @RequestParam("dateMonth") String dateMonth) {
        return ResultDTO.success(attendanceGroupService.rosterDetail(id, dateMonth));
    }

    @ApiOperation(value = "查询节假日信息", notes = "查询节假日", response = HolidayDto.class)
    @GetMapping(value = "find_date_by_month")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<List<HolidayDto>> findDateByMonth(@ApiParam(name = "排班日期yyyy-MM") @RequestParam("dateMonth") String dateMonth) {
        return ResultDTO.success(this.attendanceGroupService.findDateByMonth(dateMonth));
    }

    /**
     * 删除考勤组
     *
     * @author xinbing
     * @date 2020-07-13 20:28:37
     */
    @ApiOperation(value = "删除考勤组", notes = "修改考勤组")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> delete(@ApiParam(name = "id", value = "主键", required = true)
                               @PathVariable Long id) {
        return ResultDTO.success(attendanceGroupService.delete(id));
    }

    /**
     * 批量删除考勤组
     *
     * @author xinbing
     * @date 2020-07-13 20:28:37
     */
    @ApiOperation(value = "批量删除考勤组")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ATTENDANCE_SETTINGS')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        return ResultDTO.success(attendanceGroupService.batchDelete(ids));
    }
}