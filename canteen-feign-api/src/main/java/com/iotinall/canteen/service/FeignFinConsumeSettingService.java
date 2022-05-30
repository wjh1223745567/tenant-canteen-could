package com.iotinall.canteen.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 餐厅设置
 */
@FeignClient(value = "canteen-menu", contextId = "fin-consume-setting")
public interface FeignFinConsumeSettingService {

    @GetMapping(value = "/fin/consume-settings/feign/consume_setting")
    String mealTime(@RequestParam(value = "dataSource") String dataSource);
}
