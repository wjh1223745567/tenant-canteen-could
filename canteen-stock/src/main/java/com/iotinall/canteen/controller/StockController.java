package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.stock.StockQueryReq;
import com.iotinall.canteen.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 库存控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_STOCK)
@RestController
@RequestMapping("stock")
public class StockController {
    @Resource
    private StockService stockService;

    @ApiOperation(value = "库存分页列表", notes = "库存分页列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO page(@ModelAttribute StockQueryReq req, Pageable page) {
        return ResultDTO.success(stockService.page(req, page));
    }

    @ApiOperation(value = "查询库存明细列表", notes = "查询库存明细列表,只返回库存>0的商品", httpMethod = "GET")
    @GetMapping("detail-list")
    public ResultDTO stockDetail(@ModelAttribute StockQueryReq req, Pageable page) {
        return ResultDTO.success(stockService.getStockDetailList(req, page));
    }
}
