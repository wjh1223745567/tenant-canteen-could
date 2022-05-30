package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.shopping.ShoppingCartReq;
import com.iotinall.canteen.service.ShoppingCartService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 购物车接口
 *
 * @author WJH
 * @date 2019/11/2313:50
 */
@Api(tags = SwaggerModule.MODULE_PAY)
@RestController
@RequestMapping(value = "shopping_cart")
public class AppShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping
    public ResultDTO findCart() {
        return ResultDTO.success(this.shoppingCartService.findShoppingCart(SecurityUtils.getUserId()));
    }

    @PutMapping
    public ResultDTO updateCart(@Valid @RequestBody ShoppingCartReq shoppingCartReqs) {
        this.shoppingCartService.updateShoppingCart(shoppingCartReqs);
        return ResultDTO.success();
    }

    @DeleteMapping
    public ResultDTO deletedCart() {
        this.shoppingCartService.removeShoppingCart();
        return ResultDTO.success();
    }
}
