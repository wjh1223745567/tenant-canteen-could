package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.nutritionenc.FeignChronicDiseaseDto;
import com.iotinall.canteen.dto.nutritionenc.FeignSimMessProdReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(value = "canteen-nutritionenc", contextId = "chronic-disease")
public interface FeignChronicDiseaseService {


    @GetMapping(value = "/chronic_disease/findDtoById/{id}")
    FeignChronicDiseaseDto findDtoById(@PathVariable(value = "id") Long id);

    @PostMapping(value = "/chronic_disease/findByIds")
    List<FeignChronicDiseaseDto> findByIds(@RequestBody Set<Long> ids);

    @PostMapping(value = "/chronic_disease/cacheIsTheDiseaseUsed")
    Integer cacheIsTheDiseaseUsed(@RequestBody FeignSimMessProdReq req);
}
