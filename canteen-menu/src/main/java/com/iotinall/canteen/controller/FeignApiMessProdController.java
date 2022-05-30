package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import com.iotinall.canteen.service.SysMessProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 提供对内接口
 */
@RestController
@RequestMapping(value = "messProduct/feign")
public class FeignApiMessProdController {

    @Resource
    private SysMessProductService sysMessProductService;

    @GetMapping(value = "findById/{id}")
    public NutritionNone findById(@PathVariable(value = "id") Long id){
        return sysMessProductService.findById(id);
    }

    @GetMapping(value = "findDtoById/{id}")
    public FeignMessProdDto findDtoById(@PathVariable(value = "id") Long id){
        return sysMessProductService.findDtoById(id);
    }

    @GetMapping(value = "countCuisineMessProduct/{cuisineId}")
    public Integer countCuisineMessProduct(@PathVariable(value = "cuisineId") String cuisineId){
        return sysMessProductService.countCuisineMessProduct(cuisineId);
    }
}
