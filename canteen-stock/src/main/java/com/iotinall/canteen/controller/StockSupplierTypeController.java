package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.suppliertype.StockSupplierV2TypeAddReq;
import com.iotinall.canteen.dto.suppliertype.StockSupplierV2TypeEditReq;
import com.iotinall.canteen.service.StockSupplierTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商类型控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_SUPPLIER)
@RestController
@RequestMapping("stock/supplier/type")
public class StockSupplierTypeController {
    @Autowired
    private StockSupplierTypeService stockSupplierTypeService;

    @ApiOperation(value = "供应商类型分页查询列表", notes = "供应商类型分页查询列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO page(Pageable pageable) {
        return ResultDTO.success(this.stockSupplierTypeService.page(pageable));
    }

    @ApiOperation(value = "添加供应商类型", notes = "添加供应商类型", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_SUPPLIER_TYPE','STOCK_SUPPLIER_TYPE_EDIT')")
    public ResultDTO create(@RequestBody StockSupplierV2TypeAddReq req) {
        this.stockSupplierTypeService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "修改供应商类型", notes = "修改供应商类型", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_SUPPLIER_TYPE','STOCK_SUPPLIER_TYPE_EDIT')")
    public ResultDTO update(@RequestBody StockSupplierV2TypeEditReq req) {
        this.stockSupplierTypeService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "批量删除供应商类型", notes = "批量删除供应商类型", httpMethod = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_SUPPLIER_TYPE','STOCK_SUPPLIER_TYPE_EDIT')")
    public ResultDTO delete(@ApiParam(value = "需要删除供应商类别的id", type = "array") @RequestParam("batch") Long[] ids) {
        stockSupplierTypeService.batchDelete(ids);
        return ResultDTO.success();
    }
}
