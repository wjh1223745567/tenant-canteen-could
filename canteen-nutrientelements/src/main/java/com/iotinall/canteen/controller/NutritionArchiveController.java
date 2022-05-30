package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.service.NutritionCalculateBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 营养档案 ,所有统计处理类
 *
 * @author loki
 * @date 2020/04/10 15:47
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/archive")
public class NutritionArchiveController {
    @Resource
    NutritionCalculateBiz calculateBiz;

    @ApiOperation(value = "计划摄入计算(测试用)")
    @GetMapping("cal")
    public ResultDTO calculate(@RequestParam("employeeId") Long employeeId, @RequestParam(value = "date", required = false) LocalDate date, @RequestParam("queryType") Integer queryType) {
        return ResultDTO.success(calculateBiz.calculate(employeeId, date, queryType));
    }
}
