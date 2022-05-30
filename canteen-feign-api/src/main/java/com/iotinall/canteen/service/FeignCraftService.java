package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.nutritionenc.FeignCraftDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "canteen-nutritionenc", contextId = "craft")
public interface FeignCraftService {

    @GetMapping(value = "/nutritionenc_feign/craft/findDtoById/{id}")
    FeignCraftDto findDtoById(@PathVariable(value = "id") String id);

}
