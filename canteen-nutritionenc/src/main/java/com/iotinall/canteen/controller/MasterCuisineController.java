package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.service.MasterCuisineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 食谱控制器
 *
 * @author loki
 * @date 2020/12/09 20:55
 */
@Api(tags = SwaggerModule.MODULE_CUISINE_DISEASE)
@RestController
@RequestMapping(value = "/cuisine")
public class MasterCuisineController {
    @Resource
    private MasterCuisineService cuisineService;

    @ApiOperation(value = "获取食谱列表", notes = "获取食谱列表")
    @GetMapping
    public ResultDTO getRecipeTypeList() {
        return ResultDTO.success(cuisineService.getCuisineList());
    }
}
