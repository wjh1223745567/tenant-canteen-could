package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordAddReq;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordCriteria;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordDTO;
import com.iotinall.canteen.dto.operationrecord.KitchenOperationRecordEditReq;
import com.iotinall.canteen.service.KitchenOperationRecordService;
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
 * 清洗、切配记录
 *
 * @author xinbing
 * @date 2020-07-09 15:26:01
 */
@RestController
@RequestMapping("kitchen/operation-records")
public class KitchenOperationRecordController {

    @Resource
    private KitchenOperationRecordService kitchenOperationRecordService;

    @ApiOperation(value = "查询清洗、切配记录", notes = "条件查询列表")
    @GetMapping
    public ResultDTO<PageDTO<KitchenOperationRecordDTO>> list(KitchenOperationRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenOperationRecordService.list(criteria, pageable));
    }


    @ApiOperation(value = "新增清洗、切配记录", notes = "新增清洗、切配记录")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_OPERATION','APP_HCGL','APP_HCGL_QXGL','APP_HCGL_QXGL_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenOperationRecordAddReq req) {
        kitchenOperationRecordService.add(req);
        return ResultDTO.success();
    }


    @ApiOperation(value = "修改清洗、切配记录", notes = "修改清洗、切配记录")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_OPERATION','APP_HCGL','APP_HCGL_QXGL','APP_HCGL_QXGL_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenOperationRecordEditReq req) {
        kitchenOperationRecordService.update(req);
        return ResultDTO.success();
    }

    /**
     * 批量删除清洗、切配记录
     *
     * @author xinbing
     * @date 2020-07-09 15:26:01
     */
    @ApiOperation(value = "批量删除清洗、切配记录")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_OPERATION','APP_HCGL','APP_HCGL_QXGL','APP_HCGL_QXGL_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenOperationRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}