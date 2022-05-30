package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.warehouse.StockWarehouseTypeAddReq;
import com.iotinall.canteen.dto.warehouse.StockWarehouseTypeEditReq;
import com.iotinall.canteen.service.StockWarehouseTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 仓库控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_WAREHOUSE)
@RestController
@RequestMapping("stock/warehouse/type")
public class StockWarehouseTypeController {
    @Autowired
    public StockWarehouseTypeService stockWarehouseTypeService;

    @ApiOperation(value = "仓库类别列表", notes = "添加仓库类型", httpMethod = "GET")
    @GetMapping
    public ResultDTO page(Pageable page) {
        return ResultDTO.success(stockWarehouseTypeService.page(page));
    }
    
    @ApiOperation(value = "添加仓库类型", notes = "添加仓库类型", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_TYPE_MANAGE','STOCK_WAREHOUSE_TYPE_EDIT')")
    public ResultDTO create(@RequestBody StockWarehouseTypeAddReq req) {
        stockWarehouseTypeService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "编辑仓库类型", notes = "编辑仓库类型", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_TYPE_MANAGE','STOCK_WAREHOUSE_TYPE_EDIT')")
    public ResultDTO update(@RequestBody StockWarehouseTypeEditReq req) {
        stockWarehouseTypeService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "删除仓库类型", notes = "删除仓库类型", httpMethod = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_WAREHOUSE_TYPE_MANAGE','STOCK_WAREHOUSE_TYPE_EDIT')")
    public ResultDTO delete(@RequestParam("batch") Long[] ids) {
        stockWarehouseTypeService.delete(ids);
        return ResultDTO.success();
    }
}
