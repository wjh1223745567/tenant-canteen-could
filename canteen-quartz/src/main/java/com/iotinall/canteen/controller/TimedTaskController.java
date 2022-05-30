package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.JobTypeEnum;
import com.iotinall.canteen.dto.timedtask.TimedTaskReq;
import com.iotinall.canteen.dto.timedtask.TimedTaskTypeDto;
import com.iotinall.canteen.service.TimedTaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 定时任务接口
 */
@RestController
@RequestMapping(value = "timed_task")
public class TimedTaskController {

    @Resource
    private TimedTaskService timedTaskService;

    @GetMapping(value = "page")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> page(Pageable pageable){
        return ResultDTO.success(timedTaskService.page(pageable));
    }

    @GetMapping(value = "find_by_id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> findById(@PathVariable(value = "id") Long id){
        return ResultDTO.success(timedTaskService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> add(@Valid @RequestBody TimedTaskReq req){
        req.setId(null);
        timedTaskService.save(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> edit(@Valid @RequestBody TimedTaskReq req){
        if(req.getId() == null){
            throw new BizException("", "ID不能为空");
        }
        timedTaskService.save(req);
        return ResultDTO.success();
    }

    @GetMapping(value = "find_types")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> findTypes(){
        List<TimedTaskTypeDto> timedTaskTypeDtos = new ArrayList<>();
        for (JobTypeEnum value : JobTypeEnum.values()) {
            TimedTaskTypeDto timedTaskTypeDto = new TimedTaskTypeDto()
                    .setName(value.getName())
                    .setCode(value.getCode());
            timedTaskTypeDtos.add(timedTaskTypeDto);
        }
        return ResultDTO.success(timedTaskTypeDtos);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> deleted(@RequestParam(name = "batchId") Set<Long> ids){
        timedTaskService.deleted(ids);
        return ResultDTO.success();
    }
}
