package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.person.PersonAddReq;
import com.iotinall.canteen.dto.person.PersonRecordDTO;
import com.iotinall.canteen.dto.person.UpdateIntakeEnergyReq;
import com.iotinall.canteen.service.FeignNutritionPersonRecordService;
import com.iotinall.canteen.service.NutritionPersonRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户基本信息
 *
 * @author loki
 * @date 2020/04/10 14:33
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/person")
public class NutritionPersonController {
    @Resource
    NutritionPersonRecordService personService;

    @ApiOperation(value = "新建/更新用户信息")
    @PostMapping
    public ResultDTO create(@Valid @RequestBody PersonAddReq req) {
        personService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "查询用户信息(用户信息为空,代表该用户未填写营养个人档案)", response = PersonRecordDTO.class)
    @GetMapping
    public ResultDTO info() {
        return ResultDTO.success(personService.findPersonRecordDTO());
    }

    @ApiOperation(value = "更新用户计划摄入的能量", httpMethod = "PUT")
    @PutMapping(value = "intake")
    public ResultDTO updateIntakeEnergy(@RequestBody UpdateIntakeEnergyReq req) {
        personService.updateIntakeEnergy(req);
        return ResultDTO.success();
    }

    @GetMapping(value = "feign/findDisease")
    public String findDisease(){
        return personService.findDisease();
    }
}
