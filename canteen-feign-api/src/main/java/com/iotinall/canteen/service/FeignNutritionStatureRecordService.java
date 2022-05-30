package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.nutritionenc.FeignSysMaterialReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "canteen-nutrientelements", contextId = "stature-record")
public interface FeignNutritionStatureRecordService {

    @PostMapping(value = "/nutrition/stature/feign/isElementApplication")
    Boolean isElementApplication(@RequestBody List<FeignSysMaterialReq> materials);

}
