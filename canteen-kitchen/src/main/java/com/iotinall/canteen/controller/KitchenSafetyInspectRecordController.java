package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordAddReq;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordCriteria;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordDTO;
import com.iotinall.canteen.dto.safetyinspectrecord.KitchenSafetyInspectRecordEditReq;
import com.iotinall.canteen.service.KitchenSafetyInspectRecordService;
import io.swagger.annotations.Api;
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
 * 消防安全
 *
 * @author xinbing
 * @date 2020-07-10 11:10:12
 */
@RestController
@RequestMapping("kitchen/safety-inspect-records")
public class KitchenSafetyInspectRecordController {

    @Resource
    private KitchenSafetyInspectRecordService kitchenSafetyInspectRecordService;

    @ApiOperation(value = "查询消防安全", notes = "条件查询消防安全列表")
    @GetMapping
    public ResultDTO<PageDTO<KitchenSafetyInspectRecordDTO>> list(KitchenSafetyInspectRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenSafetyInspectRecordService.list(criteria, pageable));
    }

    @ApiOperation(value = "新增消防安全", notes = "新增消防安全")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_SAFETY_INSPECT','APP_HCGL','APP_HCGL_XFAQ','APP_HCGL_XFAQ_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenSafetyInspectRecordAddReq req) {
        kitchenSafetyInspectRecordService.add(req);
        return ResultDTO.success();
    }


    @ApiOperation(value = "修改消防安全", notes = "修改消防安全")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_SAFETY_INSPECT','APP_HCGL','APP_HCGL_XFAQ','APP_HCGL_XFAQ_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenSafetyInspectRecordEditReq req) {
        kitchenSafetyInspectRecordService.update(req);
        return ResultDTO.success();
    }

    /**
     * 批量删除消防安全
     *
     * @author xinbing
     * @date 2020-07-10 11:10:12
     */
    @ApiOperation(value = "批量删除消防安全")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_SAFETY_INSPECT','APP_HCGL','APP_HCGL_XFAQ','APP_HCGL_XFAQ_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenSafetyInspectRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}