package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.kitchen.FeignKitchenSampleDTO;
import com.iotinall.canteen.dto.sample.KitchenSampleAddReq;
import com.iotinall.canteen.dto.sample.KitchenSampleCriteria;
import com.iotinall.canteen.dto.sample.KitchenSampleDTO;
import com.iotinall.canteen.dto.sample.KitchenSampleEditReq;
import com.iotinall.canteen.service.KitchenSampleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 留样管理
 *
 * @author xinbing
 * @date 2020-07-06 17:09:03
 */
@RestController
@RequestMapping("kitchen/samples")
public class KitchenSampleController {

    @Resource
    private KitchenSampleService kitchenSampleService;

    @ApiOperation(value = "查询留样管理", notes = "条件查询留样管理列表")
    @GetMapping
    public ResultDTO<PageDTO<KitchenSampleDTO>> list(KitchenSampleCriteria criteria, @PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(kitchenSampleService.list(criteria, pageable));
    }

    @ApiOperation(value = "新增留样管理", notes = "新增留样管理")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_SAMPLE','APP_HCGL','APP_HCGL_LYGL','APP_HCGL_LYGL_AED')")
    public ResultDTO<?> add(@Valid @RequestBody KitchenSampleAddReq req) {
        kitchenSampleService.add(req);
        return ResultDTO.success();
    }


    @ApiOperation(value = "修改留样管理", notes = "修改留样管理")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_SAMPLE','APP_HCGL','APP_HCGL_LYGL','APP_HCGL_LYGL_AED')")
    public ResultDTO<?> update(@Valid @RequestBody KitchenSampleEditReq req) {
        kitchenSampleService.update(req);
        return ResultDTO.success();
    }

    /**
     * 批量删除留样管理
     *
     * @author xinbing
     * @date 2020-07-06 17:09:03
     */
    @ApiOperation(value = "批量删除留样管理")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','KITCHEN_ALL','KITCHEN_SAMPLE','APP_HCGL','APP_HCGL_LYGL','APP_HCGL_LYGL_AED')")
    public ResultDTO<?> deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true)
                                    @RequestParam(value = "batch") Long[] ids) {
        kitchenSampleService.batchDelete(ids);
        return ResultDTO.success();
    }

    @ApiOperation(value = "大屏-留样记录")
    @GetMapping(value = "feign/getSampleImgList")
    public List<FeignKitchenSampleDTO> getSampleImgList() {
        return kitchenSampleService.getSampleImgList();
    }
}