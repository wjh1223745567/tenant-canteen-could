package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.organization.FeignSerMoneybagReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "canteen-organization", contextId = "ser-moneybag")
public interface FeignSerMoneybagService {

    @GetMapping(value = "ser/moneybag/feign/addBalanceResult")
    void addBalanceResult(@RequestParam(value = "out_trade_no") String out_trade_no,@RequestParam(value = "success") boolean success);

    @PostMapping(value = "ser/moneybag/feign/serMoneybagPay")
    void serMoneybagPay(@RequestBody FeignSerMoneybagReq serMoneybagReq);
}
