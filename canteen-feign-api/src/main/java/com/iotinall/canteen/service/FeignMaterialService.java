package com.iotinall.canteen.service;

import com.iotinall.canteen.common.entity.NutritionNone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "canteen-nutritionenc", contextId = "material")
public interface FeignMaterialService {

    @PostMapping(value = "/nutritionenc_feign/material/findByIds")
    List<NutritionNone> findByIds(@RequestBody List<String> ids);

    @PostMapping(value = "/nutritionenc_feign/material/generateNutrition")
    NutritionNone generateNutrition(@RequestParam(value = "material") String material);
}
