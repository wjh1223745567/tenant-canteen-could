package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.stat.EnergyStatDTO;
import com.iotinall.canteen.service.NutritionStatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 营养档案 ,所有统计处理类
 *
 * @author loki
 * @date 2020/04/10 15:47
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/stat")
public class NutritionStatController {
    @Resource
    NutritionStatService nutritionStatService;

    @ApiOperation(value = "首页-我的能量")
    @GetMapping("homepage/energy")
    public ResultDTO homepageEnergy() {
        return ResultDTO.success(nutritionStatService.homepageEnergy());
    }

    @ApiOperation(value = "摄入数据统计图表", response = EnergyStatDTO.class)
    @GetMapping("intake/chart")
    public ResultDTO statIntakeChart() {
        return ResultDTO.success(nutritionStatService.statIntakeChart());
    }

    @ApiOperation(value = "摄入数据统计列表", response = EnergyStatDTO.class)
    @GetMapping("intake/list")
    public ResultDTO statIntakeList(Pageable pageable) {
        return ResultDTO.success(nutritionStatService.statIntakeList(pageable));
    }

    @ApiOperation(value = "身材数据统计图表", response = EnergyStatDTO.class)
    @GetMapping("stature/chart")
    public ResultDTO statStatureChart(@RequestParam("type") String type) {
        return ResultDTO.success(nutritionStatService.statStatureChart(type));
    }

    @ApiOperation(value = "身材数据统计列表", response = EnergyStatDTO.class)
    @GetMapping("stature/list")
    public ResultDTO statStatureList(@RequestParam("type") String type, Pageable pageable) {
        return ResultDTO.success(nutritionStatService.statStatureList(type, pageable));
    }

    @ApiOperation(value = "消耗统计图表", response = EnergyStatDTO.class)
    @GetMapping("consumption/chart")
    public ResultDTO statConsumptionChart() {
        return ResultDTO.success(nutritionStatService.statConsumptionChart());
    }

    @ApiOperation(value = "消耗统计列表", response = EnergyStatDTO.class)
    @GetMapping("consumption/list")
    public ResultDTO statConsumptionList(Pageable pageable) {
        return ResultDTO.success(nutritionStatService.statConsumptionList(pageable));
    }
}
