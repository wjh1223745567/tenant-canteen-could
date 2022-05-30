package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.diet.DietAddReq;
import com.iotinall.canteen.service.NutritionCustomDishService;
import com.iotinall.canteen.service.NutritionDietRecordService;
import com.iotinall.canteen.service.NutritionDietReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 营养档案,饮食记录
 *
 * @author loki
 * @date 2020/04/10 16:15
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/diet")
public class NutritionDietRecordController {

    @Resource
    NutritionDietReportService dietReportService;

    @Resource
    NutritionDietRecordService dietRecordService;

    @Resource
    private NutritionCustomDishService customDishService;

    @ApiOperation(value = "实时计算摄入能量/蛋白质/脂肪/碳水化合物/膳食纤维")
    @PostMapping("calculate")
    public ResultDTO calculate(@RequestBody DietAddReq req) {
        return ResultDTO.success(dietRecordService.calculate(req));
    }

    @ApiOperation(value = "饮食记录列表")
    @GetMapping("record")
    public ResultDTO records(@RequestParam LocalDate date) {
        return ResultDTO.success(dietRecordService.findDietRecords(date));
    }

    @ApiOperation(value = "添加饮食记录", response = DietAddReq.class)
    @PostMapping
    public ResultDTO create(@RequestBody DietAddReq req) {
        return ResultDTO.success(dietRecordService.create(req));
    }

    @ApiOperation(value = "添加饮食记录获取自定义食物列表")
    @GetMapping("custom/dish/list")
    public ResultDTO getCustomDishList() {
        Object obj = customDishService.findAll();
        return ResultDTO.success(obj);
    }

    @ApiOperation(value = "一键分析")
    @GetMapping("/analysis")
    public ResultDTO analysis(@RequestParam LocalDate date) {
        dietReportService.analysis(date);
        return ResultDTO.success();
    }

    @ApiOperation(value = "饮食报告")
    @GetMapping("/report")
    public ResultDTO report(@RequestParam LocalDate date) {
        return ResultDTO.success(dietReportService.getReportDetail(date));
    }
}
