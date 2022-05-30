package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.stock.StockWarningQueryReq;
import com.iotinall.canteen.service.StockWarningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存预警
 *
 * @author loki
 * @date 2020/08/25 19:20
 */
@RestController
@RequestMapping(value = "stock/warning")
public class StockWarningController {
    @Autowired
    private StockWarningService stockWarningService;

    @ApiOperation(value = "库存预警列表", notes = "库存预警分页列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO getStockWarningList(StockWarningQueryReq req, Pageable page) {
        return ResultDTO.success(stockWarningService.warningList(req, page));
    }

    @ApiOperation(value = "库存预警详情", notes = "库存预警详情", httpMethod = "GET")
    @GetMapping(value = "{id}")
    public ResultDTO detail(@PathVariable("id") Long id) {
        return ResultDTO.success(stockWarningService.getWarningDetail(id));
    }
}
