package com.iotinall.canteen.controller;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.protocol.SimpDataSource;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.goods.StockGoodsAddReq;
import com.iotinall.canteen.dto.goods.StockGoodsQueryReq;
import com.iotinall.canteen.dto.goods.StockGoodsUpdateReq;
import com.iotinall.canteen.service.FeignTenantOrganizationService;
import com.iotinall.canteen.service.StockGoodsService;
import com.iotinall.canteen.dto.stock.FeignStockProdDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 商品控制器
 *
 * @author loki
 * @date 2021/06/02 20:18
 */
@Api(tags = SwaggerModule.MODULE_GOODS)
@RestController
@RequestMapping("stock/goods")
public class StockGoodsController {
    @Resource
    private StockGoodsService stockGoodsService;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    /**
     * 查询商品列表
     */
    @ApiOperation(value = "商品列表查询", notes = "根据条件查询商品列表", httpMethod = "GET")
    @GetMapping
    public ResultDTO list(StockGoodsQueryReq req, Pageable pageable) {
        return ResultDTO.success(stockGoodsService.page(req, pageable));
    }

    /**
     * excel导入商品
     */
    @ApiOperation(value = "导入商品", notes = "导入商品")
    @PostMapping("excel")
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_GOODS_MANAGE','STOCK_GOODS_EDIT')")
    public ResultDTO excelAddGoods(@RequestParam(name = "file") MultipartFile file) {
        this.stockGoodsService.excelAddGoods(file);
        return ResultDTO.success();
    }

    /**
     * 添加类别商品
     */
    @ApiOperation(value = "添加类别商品", notes = "添加类别商品", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_GOODS_MANAGE','STOCK_GOODS_EDIT')")
    public ResultDTO create(@Valid @RequestBody StockGoodsAddReq req) {
        stockGoodsService.create(req);
        return ResultDTO.success();
    }

    /**
     * 修改类别商品
     */
    @ApiOperation(value = "修改类别商品", notes = "修改类别商品", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_GOODS_MANAGE','STOCK_GOODS_EDIT')")
    public ResultDTO<?> update(@Valid @RequestBody StockGoodsUpdateReq req) {
        stockGoodsService.update(req);
        return ResultDTO.success();
    }

    /**
     * 批量删除类别商品
     */
    @ApiOperation(value = "批量删除类别商品", notes = "批量删除类别商品", httpMethod = "DELETE")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','STOCK_ALL','STOCK_GOODS_MANAGE','STOCK_GOODS_EDIT')")
    public ResultDTO delete(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        stockGoodsService.delete(ids);
        return ResultDTO.success();
    }

    @ApiOperation(value = "商品详情", notes = "商品详情", httpMethod = "GET")
    @GetMapping("/{goodsId}")
    public ResultDTO findByGoodsId(@PathVariable("goodsId") Long goodId) {
        return ResultDTO.success(stockGoodsService.findByGoodsId(goodId));
    }

    @ApiOperation(value = "商品名称转拼音", notes = "商品名称转拼音", httpMethod = "GET")
    @GetMapping("/convert-pinyi")
    public ResultDTO convert2Pinyi() {
        stockGoodsService.convert2Pinyin();
        return ResultDTO.success();
    }

    /**
     * 获取库存原料信息
     * @param key
     * @param typeId
     * @return
     */
    @GetMapping(value = "feign/findTop10Material")
    public List<FeignStockProdDto> findTop10Material(@RequestParam(value = "key") String key,@RequestParam(value = "typeId", required = false) Long typeId){
        List<SimpDataSource> list = feignTenantOrganizationService.findStockDataSource();
        //切换成餐厅对应库存库
        if(list.size() > 0){
            DynamicDataSourceContextHolder.push(list.get(0).getDataSourceKey());
        }
        return stockGoodsService.findTop10Material(key, typeId);
    }
}
