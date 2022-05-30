package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.custom.CustomDishAddReq;
import com.iotinall.canteen.service.NutritionCustomDishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 营养档案,自定义食物
 *
 * @author loki
 * @date 2020/04/10 16:15
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/custom/dish")
public class NutritionCustomDishController {
    @Resource
    private NutritionCustomDishService customDishService;

    @ApiOperation(value = "添加自定义食物")
    @PostMapping()
    public ResultDTO<?> create(@RequestBody CustomDishAddReq req) {
        Long id = customDishService.create(req);
        return ResultDTO.success(id);
    }

    @ApiOperation(value = "删除自定义食物")
    @DeleteMapping("{id}")
    public ResultDTO<?> delete(@ApiParam(value = "自定义食物ID", required = true) @PathVariable Long id) {
        customDishService.delete(id);
        return ResultDTO.success();
    }

}
