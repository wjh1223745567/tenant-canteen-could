package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.stature.StatureAddReq;
import com.iotinall.canteen.dto.stature.StatureDTO;
import com.iotinall.canteen.dto.nutritionenc.FeignSysMaterialReq;
import com.iotinall.canteen.service.NutritionStatureRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 营养档案,身材记录处理类
 *
 * @author loki
 * @date 2020/04/10 16:15
 */
@Api(tags = SwaggerModule.MODULE_NUTRITION)
@RestController
@RequestMapping("nutrition/stature")
public class NutritionStatureRecordController {
    @Resource
    NutritionStatureRecordService statureRecordService;

    @ApiOperation(value = "添加身材记录", response = StatureAddReq.class)
    @PostMapping
    public ResultDTO create(@RequestBody StatureAddReq req) {
        statureRecordService.create(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "编辑身材记录", response = StatureAddReq.class)
    @PutMapping
    public ResultDTO update(@RequestBody StatureAddReq req) {
        statureRecordService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "删除身材记录")
    @DeleteMapping("/{id}")
    public ResultDTO delete(@PathVariable Long id){
        statureRecordService.delete(id);
        return ResultDTO.success();
    }

    @ApiOperation(value = "查询身材记录", response = StatureDTO.class)
    @GetMapping
    public ResultDTO get(@ApiParam(value = "日期,默认为今天") @RequestParam LocalDate date) {
        return ResultDTO.success(statureRecordService.findDTO(date));
    }


    @PostMapping(value = "feign/isElementApplication")
    public Boolean isElementApplication(@Valid @RequestBody List<FeignSysMaterialReq> materials){
        return statureRecordService.isElementApplication(materials);
    }
}
