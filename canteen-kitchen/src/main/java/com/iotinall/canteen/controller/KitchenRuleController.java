package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.rule.RuleAddReq;
import com.iotinall.canteen.dto.rule.RuleDTO;
import com.iotinall.canteen.dto.rule.RuleEditReq;
import com.iotinall.canteen.dto.rule.RuleListReq;
import com.iotinall.canteen.service.KitchenRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author xinbing
 * @date 2020-07-02
 */
@RestController
@RequestMapping("kitchen/rule")
public class KitchenRuleController {
    @Resource
    private KitchenRuleService kitchenRuleService;

    @GetMapping
    @ApiOperation(value = "规章制度列表")
    public ResultDTO<PageDTO<RuleDTO>> list(RuleListReq req, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenRuleService.list(req, pageable));
    }

    @PostMapping
    @ApiOperation(value = "添加制度")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_RULE','APP_HCGL','APP_HCGL_GZZD')")
    public ResultDTO<?> add(@Validated @RequestBody RuleAddReq req) {
        kitchenRuleService.add(req);
        return ResultDTO.success();
    }

    @PutMapping
    @ApiOperation(value = "修改制度")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_RULE','APP_HCGL','APP_HCGL_GZZD')")
    public ResultDTO<?> edit(@Validated @RequestBody RuleEditReq req) {
        kitchenRuleService.edit(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除制度")
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_RULE','APP_HCGL','APP_HCGL_GZZD')")
    public ResultDTO<?> del(
            @ApiParam(value = "多个id以,分割")
            @RequestParam(value = "batch") Long[] batch) {
        kitchenRuleService.del(batch);
        return ResultDTO.success();
    }
}
