package com.iotinall.canteen.service;

import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.dto.messprod.FeignMessProdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "canteen-menu", contextId = "menu")
public interface FeignMessProductService {

    @GetMapping(value = "messProduct/feign/findById/{id}")
    NutritionNone findById(@PathVariable(value = "id") Long id);

    @GetMapping(value =  "messProduct/feign/findDtoById/{id}")
    FeignMessProdDto findDtoById(@PathVariable(value = "id") Long id);

    @GetMapping(value = "messProduct/feign/countCuisineMessProduct/{cuisineId}")
    Integer countCuisineMessProduct(@PathVariable(value = "cuisineId") String id);


}
