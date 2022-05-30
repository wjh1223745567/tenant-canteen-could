package com.iotinall.canteen.service;

import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.dto.nutritionenc.FeignDishDto;
import com.iotinall.canteen.dto.nutritionenc.FeignSimMessProdReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "canteen-nutritionenc", contextId = "dish")
public interface FeignDishService {

    @GetMapping(value = "sys/dish/feign/findById/{id}")
    NutritionNone findById(@PathVariable(value = "id") String id);

    @GetMapping(value = "sys/dish/feign/findDtoById/{id}")
    FeignDishDto findDtoById(@PathVariable(value = "id") String id);

    @PostMapping(value = "/chronic_disease/cacheIsTheDiseaseUsed")
    Integer cacheIsTheDiseaseUsed(@RequestBody FeignSimMessProdReq prodReq);
}
