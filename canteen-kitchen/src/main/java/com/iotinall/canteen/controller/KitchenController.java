package com.iotinall.canteen.controller;

import com.iotinall.canteen.dto.kitchen.FeignKitchenLiveInfoDTO;
import com.iotinall.canteen.service.KitchenStatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后厨统计controller
 *
 * @author loki
 * @date 2021/7/16 10:41
 **/
@RestController
@RequestMapping("kitchen/stat")
public class KitchenController {
    @Resource
    private KitchenStatService kitchenStatService;

    /**
     * 大屏-后厨实况
     *
     * @author loki
     * @date 2021/7/16 10:20
     **/
    @GetMapping("/feign/getKitchenLiveInfo")
    public FeignKitchenLiveInfoDTO getKitchenLiveInfo() {
        return kitchenStatService.getKitchenLiveInfo();
    }
}
