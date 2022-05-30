package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.nutritionenc.FeignCuisineDto;
import com.iotinall.canteen.dto.nutritionenc.FeignMessProductCuisineDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(value = "canteen-nutritionenc", contextId = "cuisine")
public interface FeignCuisineService {

    @PostMapping(value = "nutritionenc_feign/cuisine/findAllChildrenId")
    Set<String> findAllChildrenId(@RequestBody Set<String> ids);

    @PostMapping(value = "nutritionenc_feign/cuisine/findByIds")
    List<FeignCuisineDto> findByIds(@RequestBody Set<String> ids);

    @GetMapping(value = "nutritionenc_feign/cuisine/getCuisineMessProductStat")
    List<FeignMessProductCuisineDto> getCuisineMessProductStat();
}
