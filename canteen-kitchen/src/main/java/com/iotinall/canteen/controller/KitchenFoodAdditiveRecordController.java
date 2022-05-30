package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordAddReq;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordCriteria;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordDTO;
import com.iotinall.canteen.dto.foodadditiverecord.KitchenFoodAdditiveRecordEditReq;
import com.iotinall.canteen.service.KitchenFoodAdditiveRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 添加剂记录
 *
 * @author xinbing
 * @date 2020-07-10 14:24:45
 */
@RestController
@RequestMapping("kitchen/food-additive-records")
public class KitchenFoodAdditiveRecordController {

    @Resource
    private KitchenFoodAdditiveRecordService kitchenFoodAdditiveRecordService;

    @ApiOperation(value = "查询添加剂记录", notes = "条件查询添加剂记录列表")
    @GetMapping
    public ResultDTO<PageDTO<KitchenFoodAdditiveRecordDTO>> list(KitchenFoodAdditiveRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenFoodAdditiveRecordService.list(criteria, pageable));
    }


    @ApiOperation(value = "新增添加剂记录", notes = "新增添加剂记录")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_FOOD_ADDITIVE','APP_HCGL','APP_HCGL_TJJ','APP_HCGL_TJJ_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenFoodAdditiveRecordAddReq req) {
        kitchenFoodAdditiveRecordService.add(req);
        return ResultDTO.success();
    }


    @ApiOperation(value = "修改添加剂记录", notes = "修改添加剂记录")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_FOOD_ADDITIVE','APP_HCGL','APP_HCGL_TJJ','APP_HCGL_TJJ_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenFoodAdditiveRecordEditReq req) {
        kitchenFoodAdditiveRecordService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "批量删除添加剂记录")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_FOOD_ADDITIVE','APP_HCGL','APP_HCGL_TJJ','APP_HCGL_TJJ_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenFoodAdditiveRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}