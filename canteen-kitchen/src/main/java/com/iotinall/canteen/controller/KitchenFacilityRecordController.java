package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.facilityrecord.KitchenFacilityRecordAddReq;
import com.iotinall.canteen.dto.facilityrecord.KitchenFacilityRecordCriteria;
import com.iotinall.canteen.dto.facilityrecord.KitchenFacilityRecordEditReq;
import com.iotinall.canteen.service.KitchenFacilityRecordService;
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
 * 消防设施
 *
 * @author xinbing
 * @date 2020-07-10 11:32:53
 */
@RestController
@RequestMapping("kitchen/facility-records")
public class KitchenFacilityRecordController {

    @Resource
    private KitchenFacilityRecordService kitchenFacilityRecordService;

    @ApiOperation(value = "查询设备设施", notes = "条件查询设备设施列表")
    @GetMapping
    public ResultDTO<?> list(KitchenFacilityRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenFacilityRecordService.list(criteria, pageable));
    }

    @ApiOperation(value = "新增设备设施", notes = "新增设备设施")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_FACILITY','APP_HCGL','APP_HCGL_SBSS','APP_HCGL_SBSS_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenFacilityRecordAddReq req) {
        kitchenFacilityRecordService.add(req);
        return ResultDTO.success();
    }


    @ApiOperation(value = "修改消防设施", notes = "修改设备设施")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_FACILITY','APP_HCGL','APP_HCGL_SBSS','APP_HCGL_SBSS_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenFacilityRecordEditReq req) {
        kitchenFacilityRecordService.update(req);
        return ResultDTO.success();
    }

    /**
     * 批量删除消防设施
     *
     * @author xinbing
     * @date 2020-07-10 11:32:53
     */
    @ApiOperation(value = "批量删除设备设施")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_FACILITY','APP_HCGL','APP_HCGL_SBSS','APP_HCGL_SBSS_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenFacilityRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}