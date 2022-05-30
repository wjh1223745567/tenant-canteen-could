package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateMsgConfigAddReq;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateMsgConfigEditReq;
import com.iotinall.canteen.service.EmptyPlateMsgConfigService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Set;

/**
 * 光盘行动配置请求处理类
 *
 * @author loki
 * @date 2021/02/05 16:22
 */
@RestController
@RequestMapping(value = "empty-plate/msg/config")
public class EmptyPlateMsgConfigController {
    @Resource
    private EmptyPlateMsgConfigService emptyPlateMsgConfigService;

    @GetMapping
    public ResultDTO page(@RequestParam(value = "keywords", required = false) String keywords, Pageable page) {
        return ResultDTO.success(emptyPlateMsgConfigService.page(keywords, page));
    }

    @PostMapping
    public ResultDTO create(@Valid @RequestBody EmptyPlateMsgConfigAddReq req) {
        emptyPlateMsgConfigService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    public ResultDTO update(@Valid @RequestBody EmptyPlateMsgConfigEditReq req) {
        emptyPlateMsgConfigService.update(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    public ResultDTO batchDelete(@RequestParam("batch") Long [] ids) {
        emptyPlateMsgConfigService.batchDelete(ids);
        return ResultDTO.success();
    }
}
