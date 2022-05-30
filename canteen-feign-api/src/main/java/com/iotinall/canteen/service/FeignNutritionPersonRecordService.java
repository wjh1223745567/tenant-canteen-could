package com.iotinall.canteen.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 营养元素服务器接口
 */

@Service
@FeignClient(value = "canteen-nutrientelements", contextId = "person-record")
public interface FeignNutritionPersonRecordService {

    /**
     * 查询当前用户疾病信息
     * @return
     */
    @GetMapping("/nutrition/person/feign/findDisease")
    String findDisease();

}
