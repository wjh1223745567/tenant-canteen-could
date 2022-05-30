package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.entity.OperationLog;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 操作日志
 */
@Slf4j
@RestController
@RequestMapping(value = "operation_log")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    /**
     * 分页
     * @param pageable
     * @return
     */
    @GetMapping(value = "page")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> page(@PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResultDTO.success(operationLogService.page(pageable));
    }

}
