package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.supplier.StockSupplierAddReq;
import com.iotinall.canteen.dto.supplier.StockSupplierEditReq;
import com.iotinall.canteen.dto.supplier.StockSupplierV2Req;
import com.iotinall.canteen.service.StockSupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_SUPPLIER)
@RestController
@RequestMapping("stock/supplier")
public class StockSupplierController {
    @Autowired
    private StockSupplierService stockSupplierService;

    @ApiOperation(value = "供应商分页查询列表", notes = "供应商分页查询列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO page(StockSupplierV2Req req, Pageable pageable) {
        return ResultDTO.success(this.stockSupplierService.page(req, pageable));
    }

    @ApiOperation(value = "添加供应商", notes = "添加供应商", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_SUPPLIER_MANAGE','STOCK_SUPPLIER_EDIT')")
    public ResultDTO create(@RequestBody StockSupplierAddReq req) {
        this.stockSupplierService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "修改供应商", notes = "修改供应商", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_SUPPLIER_MANAGE','STOCK_SUPPLIER_EDIT')")
    public ResultDTO update(@RequestBody StockSupplierEditReq req) {
        this.stockSupplierService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "批量删除供应商", notes = "批量删除供应商", httpMethod = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_SUPPLIER_MANAGE','STOCK_SUPPLIER_EDIT')")
    public ResultDTO delete(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam("batch") Long[] ids) {
        this.stockSupplierService.batchDelete(ids);
        return ResultDTO.success();
    }
}
