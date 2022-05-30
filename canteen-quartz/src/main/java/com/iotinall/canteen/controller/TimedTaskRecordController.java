package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.TimedTaskRecordService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "time_task_record")
public class TimedTaskRecordController {

    @Resource
    private TimedTaskRecordService timedTaskRecordService;

    @GetMapping(value = "page")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> page(@PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResultDTO.success(this.timedTaskRecordService.page(pageable));
    }

}
