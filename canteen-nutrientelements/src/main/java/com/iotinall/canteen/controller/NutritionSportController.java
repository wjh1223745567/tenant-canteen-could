package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.sport.SportAddReq;
import com.iotinall.canteen.dto.sport.SportEditReq;
import com.iotinall.canteen.service.NutritionSportRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;

/**
 * 营养档案 运动记录
 *
 * @author loki
 * @date 2020/04/10 14:33
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/sport")
public class NutritionSportController {
    @Resource
    NutritionSportRecordService personService;

    @ApiOperation(value = "运动记录列表")
    @GetMapping
    public ResultDTO list(@RequestParam LocalDate date) {
        return ResultDTO.success(personService.list(date));
    }

    @ApiOperation(value = "添加运动记录")
    @PostMapping
    public ResultDTO create(@Valid @RequestBody SportAddReq req) {
        personService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "编辑运动记录")
    @PutMapping
    public ResultDTO update(@Valid @RequestBody SportEditReq req) {
        personService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "删除运动记录")
    @DeleteMapping("/{id}")
    public ResultDTO delete(@PathVariable Long id) {
        personService.delete(id);
        return ResultDTO.success();
    }
}
