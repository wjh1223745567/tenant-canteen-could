package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.cookrecord.CookRecordAddReq;
import com.iotinall.canteen.dto.cookrecord.CookRecordCriteria;
import com.iotinall.canteen.dto.cookrecord.CookRecordDTO;
import com.iotinall.canteen.dto.cookrecord.CookRecordEditReq;
import com.iotinall.canteen.service.KitchenCookRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "kitchen/cook-records")
public class KitchenCookRecordController {

    @Resource
    private KitchenCookRecordService kitchenCookRecordService;

    @ApiOperation(value = "查询烹饪记录", notes = "条件查询列表")
    @GetMapping
    public ResultDTO<PageDTO<CookRecordDTO>> list(CookRecordCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenCookRecordService.list(criteria, pageable));
    }


    @ApiOperation(value = "新增烹饪记录", notes = "新增烹饪记录")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_COOK','APP_HCGL','APP_HCGL_PRJG','APP_HCGL_PRJG_AED')")
    public ResultDTO<?> add(@Valid @RequestBody CookRecordAddReq req) {
        return ResultDTO.success(kitchenCookRecordService.add(req));
    }


    @ApiOperation(value = "修改烹饪记录", notes = "修改烹饪记录")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_COOK','APP_HCGL','APP_HCGL_PRJG','APP_HCGL_PRJG_AED')")
    public ResultDTO<?> update(@Valid @RequestBody CookRecordEditReq req) {
        return ResultDTO.success(kitchenCookRecordService.update(req));
    }

    /**
     * 批量删除烹饪记录
     *
     * @author xinbing
     * @date 2020-07-09 15:26:01
     */
    @ApiOperation(value = "批量删除烹饪记录")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_COOK','APP_HCGL','APP_HCGL_PRJG','APP_HCGL_PRJG_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenCookRecordService.batchDelete(ids);
        return ResultDTO.success();
    }
}
