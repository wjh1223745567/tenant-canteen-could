package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateRecordQueryCriteria;
import com.iotinall.canteen.service.EmptyPlateRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 光盘行动请求处理类
 *
 * @author loki
 * @date 2021/02/05 16:22
 */
@Slf4j
@RestController
@RequestMapping(value = "empty-plate")
public class EmptyPlateRecordController {
    @Resource
    private EmptyPlateRecordService emptyPlateRecordService;

    @GetMapping(value = "record")
    public ResultDTO recordList(EmptyPlateRecordQueryCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(emptyPlateRecordService.page(criteria, pageable));
    }

    @GetMapping(value = "analysis/record")
    public ResultDTO analysisRecordList(EmptyPlateRecordQueryCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success();
    }
}
