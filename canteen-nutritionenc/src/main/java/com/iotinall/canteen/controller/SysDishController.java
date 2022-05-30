package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.common.protocol.ResultDTO;

import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.dish.*;
import com.iotinall.canteen.dto.nutritionenc.FeignDishDto;
import com.iotinall.canteen.service.SysDishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 菜品(字典)相关接口,只做查询
 *
 * @author loki
 * @date 2020/03/25 14:53
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping("sys/dish")
public class SysDishController {
    @Autowired
    SysDishService dishService;

    /**
     * 获取菜品名称列表
     *
     * @author loki
     * @date 2020/03/25 15:23
     */
    @GetMapping("getDishList")
    public ResultDTO dishList(@ApiParam(name = "key", value = "key") @RequestParam(value = "key", defaultValue = "") String name) {
        return ResultDTO.success(dishService.findTop10Dish(name));
    }

    /**
     * 获取材料列表
     *
     * @author loki
     * @date 2020/03/25 15:23
     */
    @GetMapping("getMaterialList")
    public ResultDTO getMaterialTop10(@ApiParam(name = "key", value = "key") @RequestParam(value = "key", defaultValue = "") String name) {
        return ResultDTO.success(dishService.findTop10Material(name));
    }

    /**
     * 获取菜品工艺列表
     *
     * @author loki
     * @date 2020/03/25 15:23
     */
    @GetMapping("getCraftList")
    public ResultDTO getCraftList() {
        return ResultDTO.success(dishService.findCraftList());
    }

    /**
     * 获取菜品口味列表
     *
     * @author loki
     * @date 2020/03/25 15:23
     */
    @GetMapping("getFlavourList")
    public ResultDTO getFlavourList() {
        return ResultDTO.success(dishService.findFlavourList());
    }

    /**
     * 获取菜品类别列表
     *
     * @author loki
     * @date 2020/03/25 15:23
     */
    @GetMapping("getCuisineList")
    public ResultDTO getCuisineList() {
        return ResultDTO.success(dishService.findCuisineList());
    }

    /**
     * 获取菜品类别列表
     *
     * @author loki
     * @date 2020/03/25 15:23
     */
    @GetMapping("getCuisineTree")
    public ResultDTO getCuisineTree() {
        return ResultDTO.success(dishService.findCuisineTree());
    }

    /**
     * 修改图片路径
     *
     * @author loki
     * @date 2020/04/17 15:29
     */
    @GetMapping("updateImgUrl")
    public ResultDTO updateImgUrl(@RequestParam String type) {
        dishService.updateImgUrl(type);
        return ResultDTO.success();
    }

    /**
     * 菜谱管理/食谱百科/菜谱列表
     *
     * @author loki
     * @date 2020/08/22 10:13
     */
    @GetMapping("getDishPage")
    public ResultDTO getDishPage(@ModelAttribute SysDishQueryReq req, @PageableDefault(sort = {"createdTime"}, direction = Sort.Direction.DESC) Pageable page) {
        return ResultDTO.success(this.dishService.getDishPage(req, page));
    }


    /**
     * 自动获取禁忌菜谱
     * @return
     */
    @GetMapping(value = "build_taboo_recipes")
    public ResultDTO buildTabooRecipes(){
        this.dishService.buildTabooRecipes();
        return ResultDTO.success();
    }

    /**
     * 菜谱管理/食谱百科/菜谱列表
     *
     * @author loki
     * @date 2020/08/22 10:13
     */
    @GetMapping("updatePractice")
    public ResultDTO updatePractice() {
        this.dishService.updatePractice();
        return ResultDTO.success();
    }

    /**
     * 添加原菜品
     *
     * @author loki
     * @date 2020/08/22 14:32
     */
    @PostMapping("addDish")
    public ResultDTO addDish(@Valid @RequestBody SysDishAddReq req) {
        return ResultDTO.success(this.dishService.addDish(req));
    }

    /**
     * 编辑原菜品
     *
     * @author loki
     * @date 2020/08/22 14:32
     */
    @PutMapping("editDish")
    public ResultDTO editDish(@Valid @RequestBody SysDishEditReq req) {
        return ResultDTO.success(this.dishService.editDish(req));
    }

    /**
     * 删除菜谱
     *
     * @return
     */
    @DeleteMapping("delDish")
    public ResultDTO delDish(@RequestParam(value = "batch") String[] ids) {
        return ResultDTO.success(this.dishService.delDish(ids));
    }

    /**
     * 菜谱管理/食谱百科/菜谱列表
     *
     * @author loki
     * @date 2020/08/22 10:13
     */
    @GetMapping("getDishMaterialPage")
    public ResultDTO getDishMaterialPage(@ModelAttribute SysMaterialQueryReq req, @PageableDefault(sort = {"createdTime"}, direction = Sort.Direction.DESC) Pageable page) {
        return ResultDTO.success(this.dishService.getDishMaterialPage(req, page));
    }

    /**
     * 添加原菜品
     *
     * @author loki
     * @date 2020/08/22 14:32
     */
    @PostMapping("addDishMaterial")
    public ResultDTO addDishMaterial(@Valid @RequestBody SysMaterialAddReq req) {
        return ResultDTO.success(this.dishService.addDishMaterial(req));
    }

    /**
     * 编辑原菜品
     *
     * @author loki
     * @date 2020/08/22 14:32
     */
    @PutMapping("editDishMaterial")
    public ResultDTO editDishMaterial(@Valid @RequestBody SysMaterialEditReq req) {
        return ResultDTO.success(this.dishService.editDishMaterial(req));
    }

    /**
     * 删除菜谱
     *
     * @return
     */
    @DeleteMapping("delDishMaterial")
    public ResultDTO delDishMaterial(@RequestParam(value = "batch") String[] ids) {
        return ResultDTO.success(this.dishService.delDishMaterial(ids));
    }

    /**
     * 菜谱管理/食谱百科/菜谱列表
     *
     * @author loki
     * @date 2020/08/22 10:13
     */
    @GetMapping("getMaterialTypeList")
    public ResultDTO getMaterialTypeList() {
        return ResultDTO.success(this.dishService.getMaterialTypeList());
    }

    /**
     * 查询营养元素
     * @param id
     * @return
     */
    @GetMapping("feign/findById/{id}")
    public NutritionNone findById(@PathVariable(value = "id") String id){
        return this.dishService.findById(id);
    }

    /**
     * 查询营养元素
     * @param id
     * @return
     */
    @GetMapping("feign/findDtoById/{id}")
    public FeignDishDto findDtoById(@PathVariable(value = "id") String id){
        return this.dishService.findDtoById(id);
    }
}