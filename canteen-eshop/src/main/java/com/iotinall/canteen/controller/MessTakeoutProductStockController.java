package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.tackout.MessProductDto;
import com.iotinall.canteen.entity.MessTakeoutProductStock;
import com.iotinall.canteen.service.MessTakeoutProductStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 外带外购商品
 *
 * @author WJH
 * @date 2019/11/2315:08
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping(value = "mess_takeout_prod")
public class MessTakeoutProductStockController {
    @Resource
    private MessTakeoutProductStockService messProductService;

    @GetMapping
    public ResultDTO<?> findProduct(String name, Long productTypeId) {
        Long empid = SecurityUtils.getUserId();
        List<MessProductDto> product = this.messProductService.findProduct(empid, name, productTypeId);
        product = product.stream().sorted(Comparator.comparing(MessProductDto::getStock).reversed()).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("MESS_PRODUCT", product);
        Integer allCount = product.stream().mapToInt(item -> item.getNumberOfShoppingCarts() != null ? item.getNumberOfShoppingCarts() : 0).sum();
        result.put("allCount", allCount);
        return ResultDTO.success(result);
    }

    @GetMapping(value = "find_takeout_product_name")
    public ResultDTO<?> findTakeoutProductName(String name) {
        return ResultDTO.success(this.messProductService.findTakeoutProductName(name));
    }

    /**
     * 搜索网上商城商品随机图片
     *
     * @return
     */
    @GetMapping(value = "find_product_images")
    public ResultDTO<?> findProductImages() {
        return ResultDTO.success(this.messProductService.findProductImages());
    }

    /**
     * 查询商品详情
     *
     * @param tackOutId
     * @return
     */
    @GetMapping(value = "findById")
    public ResultDTO<?> findById(@RequestParam Long tackOutId) {
        return ResultDTO.success(this.messProductService.findById(tackOutId));
    }

    /**
     * 查询所有商品类型
     *
     * @return
     */
    @GetMapping(value = "find_product_type")
    public ResultDTO<?> findProductType() {
        return ResultDTO.success(this.messProductService.findAllType());
    }

}
