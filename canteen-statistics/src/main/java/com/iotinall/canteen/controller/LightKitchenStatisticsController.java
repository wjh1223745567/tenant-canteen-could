package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.FeignLightManageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 明厨亮灶统计controller
 *
 * @author loki
 * @date 2021/7/12 18:32
 **/
@RestController
@RequestMapping("statistics/light-kitchen")
public class LightKitchenStatisticsController {
    @Resource
    private FeignLightManageService feignLightManageService;

    /**
     * 大屏-名厨亮灶,返回最近12张
     *
     * @author loki
     * @date 2021/7/13 15:13
     **/
    @GetMapping("getLightKitchenLatest12")
    public ResultDTO getLightKitchenLatest12() {
        return ResultDTO.success(feignLightManageService.getLightKitchenLatest12());
    }
}
