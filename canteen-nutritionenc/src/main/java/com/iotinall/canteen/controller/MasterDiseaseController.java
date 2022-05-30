package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.nutritionenc.FeignDishDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.service.FeignDishService;
import com.iotinall.canteen.service.FeignEmployeeService;
import com.iotinall.canteen.service.MasterDiseaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 慢性疾病控制器
 *
 * @author loki
 * @date 2020/12/09 20:55
 */
@Api(tags = SwaggerModule.MODULE_CUISINE_DISEASE)
@RestController
@RequestMapping(value = "/disease")
public class MasterDiseaseController {
    @Resource
    private MasterDiseaseService masterDiseaseService;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    @Resource
    private FeignDishService feignDishService;

    @ApiOperation(value = "获取疾病列表", notes = "获取疾病列表")
    @GetMapping
    public ResultDTO getDiseaseList() {
        return ResultDTO.success(masterDiseaseService.getDiseaseList());
    }

    @ApiOperation(value = "获取疾病列表", notes = "获取疾病列表")
    @GetMapping(value = "app")
    public ResultDTO getAppDiseaseList() {
        return ResultDTO.success(masterDiseaseService.getAppDiseaseList());
    }
}
