package com.iotinall.canteen.service;

import com.iotinall.canteen.common.entity.NutritionNone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "canteen-nutritionenc", contextId = "quartz-dish")
public interface FeignFromQuartzDishService {

    @Transactional
    @GetMapping(value = "sys/dish/feign/findById/{id}")
    NutritionNone findById(@PathVariable(value = "id") String id, @RequestHeader(value = "token") String token);
}
