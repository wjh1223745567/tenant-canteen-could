package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.nutritionenc.FeignFlavourDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "canteen-nutritionenc", contextId = "flavour")
public interface FeignFlavourService {

    @GetMapping(value = "/nutritionenc_feign/flavour/findDtoById/{id}")
    FeignFlavourDto findDtoById(@PathVariable(value = "id") String id);
}
