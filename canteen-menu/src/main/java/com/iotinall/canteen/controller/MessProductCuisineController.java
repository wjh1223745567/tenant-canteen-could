package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.service.FeignCuisineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜品类别
 *
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping("mess/product/cuisine")
public class MessProductCuisineController {

    @Resource
    private FeignCuisineService feignCuisineService;

    @ApiOperation(value = "获取菜品分类列表", response = List.class)
    @GetMapping
    public ResultDTO list() {
        return ResultDTO.success(feignCuisineService.getCuisineMessProductStat());
    }
}