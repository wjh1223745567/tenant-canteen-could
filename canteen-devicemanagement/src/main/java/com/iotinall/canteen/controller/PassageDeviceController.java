package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.passage.PassageAddReq;
import com.iotinall.canteen.dto.passage.PassageEditReq;
import com.iotinall.canteen.service.PassageDeviceService;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 通行设备
 *
 * @author loki
 * @date 2021/7/9 10:43
 **/
@RestController
@RequestMapping("device/passage")
public class PassageDeviceController {
    @Resource
    private PassageDeviceService passageDeviceService;

    @GetMapping
    public ResultDTO page(@RequestParam(value = "keywords",required = false) String keywords,
                          @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC)Pageable pageable) {
        return ResultDTO.success(passageDeviceService.page(keywords, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_PASSAGE_EDIT')")
    public ResultDTO create(@RequestBody @Valid PassageAddReq req) {
        passageDeviceService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_PASSAGE_EDIT')")
    public ResultDTO update(@RequestBody @Valid PassageEditReq req) {
        passageDeviceService.update(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_DEVICE','DEVICE_PASSAGE_EDIT')")
    public ResultDTO batchDelete(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        passageDeviceService.batchDelete(ids);
        return ResultDTO.success();
    }
}
