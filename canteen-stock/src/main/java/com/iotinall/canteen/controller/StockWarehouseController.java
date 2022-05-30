package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.warehouse.StockWarehouseAddReq;
import com.iotinall.canteen.dto.warehouse.StockWarehouseEditReq;
import com.iotinall.canteen.dto.warehouse.StockWarehouseLocationAddReq;
import com.iotinall.canteen.dto.warehouse.StockWarehouseLocationEditReq;
import com.iotinall.canteen.service.StockWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 仓库控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_WAREHOUSE)
@RestController
@RequestMapping("stock/warehouse")
public class StockWarehouseController {
    @Resource
    private StockWarehouseService stockWarehouseService;

    @ApiOperation(value = "仓库树", notes = "仓库位置树", httpMethod = "GET")
    @GetMapping("tree")
    public ResultDTO tree() {
        return ResultDTO.success(stockWarehouseService.tree());
    }

    @ApiOperation(value = "添加仓库", notes = "添加仓库", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_MANAGE','STOCK_WAREHOUSE_EDIT')")
    public ResultDTO create(@RequestBody StockWarehouseAddReq req) {
        stockWarehouseService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "编辑仓库", notes = "编辑仓库", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_MANAGE','STOCK_WAREHOUSE_EDIT')")
    public ResultDTO update(@RequestBody StockWarehouseEditReq req) {
        stockWarehouseService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "添加仓库位置", notes = "添加仓库位置", httpMethod = "POST")
    @PostMapping("location")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_MANAGE','STOCK_WAREHOUSE_EDIT')")
    public ResultDTO create(@RequestBody StockWarehouseLocationAddReq req) {
        stockWarehouseService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "编辑仓库位置", notes = "编辑仓库位置", httpMethod = "PUT")
    @PutMapping("location")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_MANAGE','STOCK_WAREHOUSE_EDIT')")
    public ResultDTO update(@RequestBody StockWarehouseLocationEditReq req) {
        stockWarehouseService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "删除仓库或者仓库位置", notes = "删除仓库或者仓库位置", httpMethod = "DELETE")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_MANAGE','STOCK_WAREHOUSE_EDIT')")
    public ResultDTO delete(@PathVariable("id") Long id) {
        stockWarehouseService.delete(id);
        return ResultDTO.success();
    }
}
