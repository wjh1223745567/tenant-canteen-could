package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.tackout.MessTakeoutProductTypeAddReq;
import com.iotinall.canteen.dto.tackout.MessTakeoutProductTypeEditReq;
import com.iotinall.canteen.service.MessTakeoutProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = SwaggerModule.MODULE_DASHBOARD)
@RestController
@RequestMapping(value = "mess/takeout/product/type")
public class MessTakeoutProductTypeController {
    @Resource
    private MessTakeoutProductTypeService takeoutProductTypeService;

    @ApiOperation(value = "获取商品类别树", notes = "获取商品类别树", httpMethod = "GET")
    @GetMapping("tree")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL','TAKEOUT_PRODUCT_TYPE_EDIT','TAKEOUT_PRODUCT_LIST')")
    public ResultDTO tree() {
        return ResultDTO.success(takeoutProductTypeService.tree());
    }

    @ApiOperation(value = "添加商品类别", notes = "添加商品类别", httpMethod = "POST")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL','TAKEOUT_PRODUCT_TYPE_EDIT')")
    public ResultDTO create(@Valid @RequestBody MessTakeoutProductTypeAddReq req) {
        return ResultDTO.success(this.takeoutProductTypeService.create(req));
    }

    @ApiOperation(value = "修改商品类别", notes = "修改商品类别", httpMethod = "PUT")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL','TAKEOUT_PRODUCT_TYPE_EDIT')")
    public ResultDTO update(@Valid @RequestBody MessTakeoutProductTypeEditReq req) {
        this.takeoutProductTypeService.update(req);
        return ResultDTO.success();
    }

    @ApiOperation(value = "删除商品类型", notes = "删除商品类型", httpMethod = "DELETE")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAKE_OUT_ALL','TAKEOUT_PRODUCT_TYPE_EDIT')")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id",
            required = true) @PathVariable Long id) {
        this.takeoutProductTypeService.delete(id);
        return ResultDTO.success();
    }
}
