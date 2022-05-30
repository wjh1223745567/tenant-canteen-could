package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;

import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.dish.DishSimpleDTO;
import com.iotinall.canteen.service.AppSysDishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 营养档案-营养调理
 *
 * @author loki
 * @date 2020/04/13 19:06
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/condition")
public class NutritionConditionController {
    @Resource
    AppSysDishService dishService;

    @ApiOperation(value = "获取菜谱列表", response = DishSimpleDTO.class)
    @GetMapping
    public ResultDTO getNutritionConditionDish(String name, @ApiParam(value = "菜系CODE", required = true) String code, Pageable pageable) {
        return ResultDTO.success(dishService.findNutritionConditionDish(name, code, pageable));
    }

    @GetMapping(value = "page_name")
    public ResultDTO getNutritionConditionDish(String name, Pageable pageable) {
        return ResultDTO.success(dishService.findNutritionConditionDish(name, pageable));
    }

    @ApiOperation(value = "获取菜谱详情", response = DishSimpleDTO.class)
    @GetMapping("dish/info/{id}")
    public ResultDTO getNutritionConditionDishDetail(@PathVariable String id) {
        return ResultDTO.success(dishService.findNutritionConditionDishDetail(id));
    }

    @GetMapping("images")
    public ResultDTO findNutritionImgs(){
        return ResultDTO.success(dishService.findRandomImages());
    }

    @GetMapping(value = "dish/product_names")
    public ResultDTO productNames(String name){
        return ResultDTO.success(dishService.productNames(name));
    }

    @ApiOperation(value = "获取营养成分排名菜谱", response = DishSimpleDTO.class)
    @GetMapping("dish/rank")
    public ResultDTO getDishListByNutritionRank(@RequestParam("type") String type, @RequestParam("name") String name, Pageable pageable) {
        return ResultDTO.success(dishService.findDishListByNutritionRank(type, name, pageable));
    }

    @ApiModelProperty(value = "获取营养成分排名素材")
    @GetMapping("dish_material_rank")
    public ResultDTO getMaterialByNutritionRank(@RequestParam("type") String type, @RequestParam("name") String name, Pageable pageable) {
        return ResultDTO.success(this.dishService.getMaterialByNutritionRank(type, name, pageable));
    }

    @ApiModelProperty(value = "原料详情")
    @GetMapping("material/{id}")
    public ResultDTO getMaterialView(@PathVariable String id){
        return ResultDTO.success(this.dishService.getMaterialView(id));
    }

    @ApiOperation(value = "查找自定义食物选择系统菜品列表")
    @GetMapping("list/10")
    public ResultDTO<?> getSysDishList(String name) {
        return ResultDTO.success(dishService.findTop10Dish(name));
    }


}
