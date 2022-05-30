package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.flwconfig.FlwConfigUpdateReq;
import com.iotinall.canteen.service.StockFlwConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 流程配置控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_FLOW_CONFIG)
@RestController
@RequestMapping("stock/flw/config")
public class StockFlwConfigController {
    @Resource
    private StockFlwConfigService stockFlwConfigService;

    @ApiOperation(value = "获取流程配置", notes = "获取流程配置", httpMethod = "GET")
    @GetMapping
    public ResultDTO findType(@ApiParam(value = "流程类型 stock_in-采购入库  stock_in_back-采购退货 stock_out-领用出库 stock_out_back-领用退库 stock_inventory-库存盘点") @RequestParam String type) {
        return ResultDTO.success(stockFlwConfigService.findType(type));
    }

    @ApiOperation(value = "保存流程配置", notes = "保存流程配置", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_FLW_MANAGE','STOCK_FLW_SETTING')")
    public ResultDTO update(@RequestBody FlwConfigUpdateReq req) {
        this.stockFlwConfigService.update(req);
        return ResultDTO.success();
    }
}
