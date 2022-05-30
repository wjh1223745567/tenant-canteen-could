package com.iotinall.canteen.controller;


import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.SwaggerModule;
import com.iotinall.canteen.dto.messprod.*;
import com.iotinall.canteen.dto.stock.FeignProcurementDto;
import com.iotinall.canteen.service.FeignTenantOrganizationService;
import com.iotinall.canteen.service.SysMessProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 菜品 Controller
 *
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Api(tags = SwaggerModule.MODULE_PRODUCT)
@RestController
@RequestMapping("mess/products")
public class MessProductController {

    @Resource
    private SysMessProductService messProductService;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    /**
     * 查询菜品列表
     *
     * @author xin-bing
     * @date 2019-10-21 16:07:57
     */
    @ApiOperation(value = "查询菜品", notes = "根据条件查询菜品列表")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT')")
    public ResultDTO<PageDTO<SysMessProductDTO>> list(MessProductQueryCriteria criteria, @PageableDefault(sort = {"p.createTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultDTO.success(messProductService.pageMessProduct(criteria, pageable));
    }


    /**
     * 菜品详情
     *
     * @author xin-bing
     * @date 2019-10-21 16:07:57
     */
    @ApiOperation(value = "详情", notes = "菜品详情")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT')")
    public ResultDTO<SysMessProductDetailDTO> detail(@ApiParam(name = "id", value = "id", required = true) @PathVariable Long id) {
        return ResultDTO.success(messProductService.detail(id));
    }

    /**
     * 菜品评论
     *
     * @return
     */
    @ApiOperation(value = "员工评论", notes = "员工评论")
    @GetMapping(value = "/{id}/comments")
    public ResultDTO<CursorPageDTO<SysMessProductCommentDTO>> getProductComments(@ApiParam(value = "产品id") @PathVariable(value = "id") Long productId,
                                                                                 @RequestParam(value = "tags") String tags,
                                                                                 @ApiParam(value = "cursor") @RequestParam(value = "cursor", required = false) Long cursor) {
        return ResultDTO.success(messProductService.pageComments(productId, cursor, tags));
    }

    /**
     * 获取原料类型
     * @param name
     * @param typeId
     * @return
     */
    @GetMapping("/getMaterialList")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_EDIT')")
    public ResultDTO getMaterialTop10(@ApiParam(name = "key", value = "key") @RequestParam(value = "key", defaultValue = "") String name, @RequestParam(required = false) Long typeId) {
        return ResultDTO.success(messProductService.findTop10Material(name, typeId));
    }

    @GetMapping("/simpDataSources")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_EDIT')")
    public ResultDTO<?> simpDataSources(){
        return ResultDTO.success(messProductService.simpDataSources());
    }

    /**
     * 新增菜品
     *
     * @author xin-bing
     * @date 2019-10-21 16:07:57
     */
    @ApiOperation(value = "新增菜品", notes = "新增菜品")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_EDIT')")
    public ResultDTO create(@Valid @RequestBody SysMessProductAddReq messProductAddReq) {
        return ResultDTO.success(messProductService.add(messProductAddReq));
    }

    /**
     * 推送数据到百度菜谱
     *
     * @return
     */
    @GetMapping(value = "push_all_to_baidu")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_EDIT')")
    public ResultDTO pushAllToBaidu() {
        this.messProductService.pushAllToBaidu();
        return ResultDTO.success();
    }

    /**
     * 修改菜品
     *
     * @author xin-bing
     * @date 2019-10-21 16:07:57
     */
    @ApiOperation(value = "修改菜品", notes = "修改菜品")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_EDIT')")
    public ResultDTO update(@Valid @RequestBody SysMessProductEditReq messProductEditReq) {
        return ResultDTO.success(messProductService.update(messProductEditReq));
    }

    /**
     * 菜品上下架
     *
     * @param req
     * @return
     */
    @ApiOperation(value = "上下架", notes = "菜品上下架")
    @PutMapping(value = "toggle")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_TOGGLE')")
    public ResultDTO toggle(@Valid @RequestBody MessProductToggleReq req) {
        return ResultDTO.success(messProductService.toggle(req));
    }

    /**
     * 删除菜品
     *
     * @author xin-bing
     * @date 2019-10-21 16:07:57
     */
    @ApiOperation(value = "删除菜品", notes = "修改菜品")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_DELETE')")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id", required = true) @PathVariable Long id) {
        return ResultDTO.success(messProductService.delete(id));
    }

    /**
     * 批量删除菜品
     *
     * @author xin-bing
     * @date 2019-10-21 16:07:57
     */
    @ApiOperation(value = "批量删除菜品")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_PRODUCT_DELETE')")
    public ResultDTO deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        messProductService.batchDelete(ids);
        return ResultDTO.success();
    }


    @ApiOperation(value = "根据标签统计点评数")
    @GetMapping(value = "countComment/{productId}")
    public ResultDTO countComment(@PathVariable(value = "productId") Long productId) {
        return ResultDTO.success(this.messProductService.countProductComments(productId));
    }

    @GetMapping(value = "feign/traceabilityChain")
    public FeignProcurementDto.MenuProd traceabilityChain(@RequestParam(value = "id") Long id){
        //切换数据源
        String dataSource = feignTenantOrganizationService.findMenuDataSource();
        if(StringUtils.isBlank(dataSource)){
            throw new BizException("", "未知餐厅数据源");
        }
        DynamicDataSourceContextHolder.push(dataSource);
        return this.messProductService.traceabilityChain(id);
    }
}