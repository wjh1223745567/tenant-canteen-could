package com.iotinall.canteen.controller;

import com.iotinall.canteen.service.FeignLightManageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 光盘行动controller
 *
 * @author loki
 * @date 2021/7/12 18:31
 **/
@RestController
@RequestMapping(value = "statistics/empty-plate")
public class EmptyPlateStatisticsController {
    @Resource
    private FeignLightManageService feignLightManageService;
}
