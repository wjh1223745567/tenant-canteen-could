package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.storehouse.FeignTenantSubOrganizationDto;
import com.iotinall.canteen.dto.tenantsuborginfo.TenantSubOrgInfoCondition;
import com.iotinall.canteen.dto.tenantsuborginfo.TenantSubOrgInfoReq;
import com.iotinall.canteen.service.TenantSubOrgInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 餐厅子食堂
 */
@RestController
@RequestMapping(value = "tenant_sub_org_info")
public class TenantSubOrgInfoController {

    @Resource
    private TenantSubOrgInfoService tenantSubOrgInfoService;

    /**
     * 分页
     *
     * @param condition
     * @param pageable
     * @return
     */
    @GetMapping(value = "page")
    public ResultDTO<?> page(TenantSubOrgInfoCondition condition, Pageable pageable) {
        return ResultDTO.success(tenantSubOrgInfoService.page(condition, pageable));
    }

    /**
     * 不分页
     *
     * @return
     */
    @GetMapping(value = "all")
    public ResultDTO<?> all() {
        return ResultDTO.success(tenantSubOrgInfoService.all());
    }

    /**
     * 查询所有小程序使用
     *
     * @return
     */
    @GetMapping(value = "find_all")
    public ResultDTO<?> findAll(TenantSubOrgInfoCondition condition) {
        return ResultDTO.success(tenantSubOrgInfoService.findAll(condition));
    }

    @GetMapping(value = "view")
    public ResultDTO<?> view(@RequestParam(value = "id") Long id, @RequestParam(value = "longitude", required = false) BigDecimal longitude, @RequestParam(value = "latitude", required = false) BigDecimal latitude) {
        return ResultDTO.success(tenantSubOrgInfoService.view(id, longitude, latitude));
    }

    @PostMapping(value = "save")
    @PreAuthorize("hasAnyRole('ADMIN','CANTEEN_ALL', 'CANTEEN_SUB', 'CANTEEN_SUB_EDIT')")
    public ResultDTO<?> save(@RequestBody @Valid TenantSubOrgInfoReq req) {
        tenantSubOrgInfoService.save(req);
        return ResultDTO.success();
    }

    @GetMapping(value = "feign/findById/{id}")
    public FeignTenantSubOrganizationDto getById(@PathVariable(value = "id") Long id) {
        return tenantSubOrgInfoService.findById(id);
    }
}
