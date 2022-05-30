package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.goodstype.GoodsTypeAddReq;
import com.iotinall.canteen.dto.goodstype.GoodsTypeUpdateReq;
import com.iotinall.canteen.service.StockGoodsTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 商品控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_GOODS)
@RestController
@RequestMapping("stock/goods/type")
public class StockGoodsTypeController {
    @Autowired
    private StockGoodsTypeService stockGoodsTypeService;

    @ApiOperation(value = "获取商品类别树", notes = "获取商品类别树", httpMethod = "GET")
    @GetMapping("tree")
    public ResultDTO tree() {
        return ResultDTO.success(stockGoodsTypeService.tree());
    }


    @ApiOperation(value = "获取所有商品类别", notes = "获取所有商品类别", httpMethod = "GET")
    @GetMapping("all")
    public ResultDTO all() {
        return ResultDTO.success(stockGoodsTypeService.all());
    }


    @ApiOperation(value = "添加商品类别", notes = "添加商品类别", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_GOODS_MANAGE','STOCK_GOODS_TYPE_EDIT')")
    public ResultDTO create(@Valid @RequestBody GoodsTypeAddReq req) {
        stockGoodsTypeService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "编辑商品类别", notes = "编辑商品类别", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_GOODS_MANAGE','STOCK_GOODS_TYPE_EDIT')")
    public ResultDTO update(@Valid @RequestBody GoodsTypeUpdateReq req) {
        stockGoodsTypeService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "删除商品类别", notes = "删除商品类别", httpMethod = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_GOODS_MANAGE','STOCK_GOODS_TYPE_EDIT')")
    public ResultDTO delete(@ApiParam(value = "需要删除商品类别的id", type = "array") @RequestParam("batch") Long[] ids) {
        return ResultDTO.success(stockGoodsTypeService.delete(ids));
    }
}
