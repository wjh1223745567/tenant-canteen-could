package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.disinfect.KitchenDisinfectAddReq;
import com.iotinall.canteen.dto.disinfect.KitchenDisinfectCriteria;
import com.iotinall.canteen.dto.disinfect.KitchenDisinfectDTO;
import com.iotinall.canteen.dto.disinfect.KitchenDisinfectEditReq;
import com.iotinall.canteen.service.KitchenDisinfectService;
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
 * 消毒管理
 *
 * @author xinbing
 * @date 2020-07-06 15:32:49
 */
@RestController
@RequestMapping("/kitchen/disinfects")
public class KitchenDisinfectController {

    @Resource
    private KitchenDisinfectService kitchenDisinfectService;

    @ApiOperation(value = "查询消毒管理", notes = "条件查询消毒管理列表")
    @GetMapping
    public ResultDTO<PageDTO<KitchenDisinfectDTO>> list(KitchenDisinfectCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenDisinfectService.list(criteria, pageable));
    }

    @ApiOperation(value = "新增消毒管理", notes = "新增消毒管理")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_DISINFECT','APP_HCGL','APP_HCGL_XDGL','APP_HCGL_XDGL_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenDisinfectAddReq req) {
        kitchenDisinfectService.add(req);
        return ResultDTO.success();
    }


    @ApiOperation(value = "修改消毒管理", notes = "修改消毒管理")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_DISINFECT','APP_HCGL','APP_HCGL_XDGL','APP_HCGL_XDGL_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenDisinfectEditReq req) {
        kitchenDisinfectService.update(req);
        return ResultDTO.success();
    }


    /**
     * 批量删除消毒管理
     *
     * @author xinbing
     * @date 2020-07-06 15:32:49
     */
    @ApiOperation(value = "批量删除消毒管理")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_DISINFECT','APP_HCGL','APP_HCGL_XDGL','APP_HCGL_XDGL_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenDisinfectService.batchDelete(ids);
        return ResultDTO.success();
    }

}