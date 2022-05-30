package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.HaircutOrderAddReq;
import com.iotinall.canteen.dto.HaircutOrderDTO;
import com.iotinall.canteen.dto.HaircutOrderEditReq;
import com.iotinall.canteen.dto.HaircutOrderQueryReq;
import com.iotinall.canteen.service.HaircutOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @description:理发订单控制器
 * @author: JoeLau
 * @time: 2021年06月23日 16:33:35
 */
@Api
@RestController
@RequestMapping("/haircut/order")
public class HaircutOrderController {
    @Resource
    HaircutOrderService haircutOrderService;

    @GetMapping
    @ApiOperation(value = "查询", notes = "查询所有订单分页", response = HaircutOrderDTO.class)
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER','APP_HAIRCUT_MASTER')")
    public ResultDTO page(HaircutOrderQueryReq req, @PageableDefault(sort = {"pickTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(this.haircutOrderService.page(req, pageable));
    }

    @PostMapping
    @ApiOperation(value = "添加", notes = "添加新订单")
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER')")
    public ResultDTO create(@Valid @RequestBody HaircutOrderAddReq req) {
        this.haircutOrderService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    @ApiOperation(value = "编辑", notes = "编辑订单")
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_USER','APP_HAIRCUT_MASTER')")
    public ResultDTO update(@Valid @RequestBody HaircutOrderEditReq req) {
        this.haircutOrderService.update(req);
        return ResultDTO.success();
    }

    @GetMapping("/master")
    @ApiOperation(value = "小程序理发师订单查询")
    @PreAuthorize("hasAnyRole('ADMIN','HAIRCUT_ALL','APP_HAIRCUT','APP_HAIRCUT_MASTER')")
    public ResultDTO masterPage(Boolean status, @PageableDefault(sort = {"pickTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResultDTO.success(this.haircutOrderService.masterPage(status,pageable));
    }

}
