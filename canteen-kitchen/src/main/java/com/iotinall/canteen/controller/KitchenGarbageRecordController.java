package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.garbage.KitchenGarbageRecordAddReq;
import com.iotinall.canteen.dto.garbage.KitchenGarbageRecordCriteria;
import com.iotinall.canteen.dto.garbage.KitchenGarbageRecordDTO;
import com.iotinall.canteen.dto.garbage.KitchenGarbageRecordEditReq;
import com.iotinall.canteen.service.KitchenGarbageRecordService;
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
 * 餐厨垃圾
 *
 * @author xinbing
 * @date 2020-07-10 11:46:09
 */
@RestController
@RequestMapping("kitchen/garbage-records")
public class KitchenGarbageRecordController {

    @Resource
    private KitchenGarbageRecordService kitchenGarbageRecordService;

    @ApiOperation(value = "查询餐厨垃圾", notes = "条件查询餐厨垃圾列表")
    @GetMapping
    public ResultDTO<PageDTO<KitchenGarbageRecordDTO>> list(KitchenGarbageRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenGarbageRecordService.list(criteria, pageable));
    }

    @ApiOperation(value = "新增餐厨垃圾", notes = "新增餐厨垃圾")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_GARBAGE','APP_HCGL','APP_HCGL_CYLJ','APP_HCGL_CYLJ_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenGarbageRecordAddReq req) {
        kitchenGarbageRecordService.add(req);
        return ResultDTO.success();
    }


    @ApiOperation(value = "修改餐厨垃圾", notes = "修改餐厨垃圾")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_GARBAGE','APP_HCGL','APP_HCGL_CYLJ','APP_HCGL_CYLJ_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenGarbageRecordEditReq req) {
        kitchenGarbageRecordService.update(req);
        return ResultDTO.success();
    }


    /**
     * 批量删除餐厨垃圾
     *
     * @author xinbing
     * @date 2020-07-10 11:46:09
     */
    @ApiOperation(value = "批量删除餐厨垃圾")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_GARBAGE','APP_HCGL','APP_HCGL_CYLJ','APP_HCGL_CYLJ_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenGarbageRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}