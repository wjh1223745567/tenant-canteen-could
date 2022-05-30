package com.iotinall.canteen.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(value = "canteen-weixin", contextId = "wx")
public interface FeignWxService {

    @PostMapping(value = "wx/feign/doUnifiedOrder")
    Map<String, String> doUnifiedOrder(@RequestParam(value = "orderNo") String orderNo, @RequestParam(value = "productName") String productName, @RequestParam(value = "amount") BigDecimal amount, @RequestParam(value = "oppenid") String oppenid);
}
