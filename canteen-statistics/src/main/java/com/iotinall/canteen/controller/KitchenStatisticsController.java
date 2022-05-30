package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.FeignEmployeeService;
import com.iotinall.canteen.service.FeignKitchenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后厨模块统计
 *
 * @author loki
 * @date 2021/7/13 16:27
 **/
@RestController
@RequestMapping("statistics/kitchen")
public class KitchenStatisticsController {
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FeignKitchenService feignKitchenService;

    @GetMapping("live")
    public ResultDTO getKitchenLiveInfo() {
        return ResultDTO.success(feignKitchenService.getKitchenLiveInfo());
    }

    @GetMapping(value = "cook-list")
    public ResultDTO getCookList() {
        return ResultDTO.success(feignEmployeeService.getKitchenCookList());
    }

    @GetMapping(value = "sample-list")
    public ResultDTO getAssessSampleList() {
        return ResultDTO.success(feignKitchenService.getSampleImgList());
    }
}
