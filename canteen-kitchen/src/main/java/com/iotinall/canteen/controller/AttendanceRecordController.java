package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.attendancesrecord.AttendanceRecordCriteria;
import com.iotinall.canteen.dto.attendancesrecord.AttendanceRecordDTO;
import com.iotinall.canteen.service.AttendanceRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Map;

/**
 * 考勤组
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@RestController
@RequestMapping("kitchen/attendance-record")
public class AttendanceRecordController {
    @Resource
    private AttendanceRecordService attendanceRecordService;

    @ApiOperation(value = "查询考勤记录", notes = "条件查询记录列表")
    @GetMapping
    public ResultDTO<PageDTO<AttendanceRecordDTO>> list(AttendanceRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(attendanceRecordService.list(criteria, pageable));
    }

    @ApiOperation(value = "查询当前用户考勤记录")
    @GetMapping(value = "find_now_user")
    public ResultDTO<?> findNowUser(AttendanceRecordCriteria criteria) {
        return ResultDTO.success(attendanceRecordService.findNowUser(criteria));
    }


    @ApiOperation(value = "根据时间获取当前用户考勤时间", notes = "根据时间获取当前用户考勤时间")
    @GetMapping(value = "find_shift_time")
    public ResultDTO<Map<String, Object>> findShiftTime(@ApiParam("date 时间") @RequestParam(name = "date") LocalDate date) {
        return ResultDTO.success(this.attendanceRecordService.findShiftTime(date));
    }

    @ApiOperation(value = "导出考勤记录", notes = "导出考勤记录", httpMethod = "POST")
    @PostMapping("export")
    public void export(HttpServletResponse response, @RequestBody AttendanceRecordCriteria criteria) {
        this.attendanceRecordService.export(response, criteria);
    }

    @ApiOperation(value = "导出考勤记录合计", notes = "导出考勤记录合计", httpMethod = "POST")
    @PostMapping("export/sum")
    public void exportSum(HttpServletResponse response, @RequestBody AttendanceRecordCriteria criteria) {
        this.attendanceRecordService.exportSum(response, criteria);
    }
}