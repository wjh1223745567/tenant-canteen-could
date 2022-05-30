package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordAddReq;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordCriteria;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordDTO;
import com.iotinall.canteen.dto.envinspectrecord.KitchenEnvInspectRecordEditReq;
import com.iotinall.canteen.service.KitchenEnvInspectRecordService;
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
 * 环境卫生
 *
 * @author xinbing
 * @date 2020-07-10 13:48:40
 */
@RestController
@RequestMapping("kitchen/env-inspect-records")
public class KitchenEnvInspectRecordController {

    @Resource
    private KitchenEnvInspectRecordService kitchenEnvInspectRecordService;

    @ApiOperation(value = "查询环境卫生", notes = "条件查询环境卫生列表")
    @GetMapping
    public ResultDTO<PageDTO<KitchenEnvInspectRecordDTO>> list(KitchenEnvInspectRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenEnvInspectRecordService.list(criteria, pageable));
    }

    @ApiOperation(value = "新增环境卫生", notes = "新增环境卫生")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ENV','APP_HCGL','APP_HCGL_HJWS','APP_HCGL_HJWS_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenEnvInspectRecordAddReq req) {
        return ResultDTO.success(kitchenEnvInspectRecordService.add(req));
    }

    @ApiOperation(value = "修改环境卫生", notes = "修改环境卫生")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ENV','APP_HCGL','APP_HCGL_HJWS','APP_HCGL_HJWS_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenEnvInspectRecordEditReq req) {
        kitchenEnvInspectRecordService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "批量删除环境卫生")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_ENV','APP_HCGL','APP_HCGL_HJWS','APP_HCGL_HJWS_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenEnvInspectRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}