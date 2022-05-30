package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.protocol.SimpDataSource;
import com.iotinall.canteen.dto.tenantorg.TenantOrganizationReq;
import com.iotinall.canteen.service.TenantOrganizationService;
import com.iotinall.canteen.dto.storehouse.FeignTenantOrganizationDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "tenant_organization")
public class TenantOrganizationController {

    @Resource
    private TenantOrganizationService tenantOrganizationService;

    @GetMapping
    public ResultDTO<?> tree(@RequestParam(value = "id", required = false) Long id){
        return ResultDTO.success(tenantOrganizationService.tree(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> add(@Valid @RequestBody TenantOrganizationReq req){
        req.setId(null);
        tenantOrganizationService.save(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> edit(@Valid @RequestBody TenantOrganizationReq req){
        if(req.getId() == null){
            throw new BizException("", "ID不能为空");
        }
        tenantOrganizationService.save(req);
        return ResultDTO.success();
    }

    @GetMapping(value = "find_by_id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> findById(@PathVariable(value = "id") Long id){
        return ResultDTO.success(tenantOrganizationService.findById(id));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> deleted(@RequestParam(value = "batchId") Set<Long> ids){
        tenantOrganizationService.deleted(ids);
        return ResultDTO.success();
    }

    @GetMapping(value = "feign/findByIds")
    public Map<Long, FeignTenantOrganizationDto> findByIds(@RequestBody Set<Long> ids){
        return tenantOrganizationService.findByIds(ids);
    }

    @GetMapping(value = "feign/findAll")
    public Map<Long, FeignTenantOrganizationDto> findAll(){
        return tenantOrganizationService.findAll();
    }

    @PostMapping(value = "feign/findAllChildren")
    public Set<Long> findAllChildren(@RequestBody Set<Long> ids){
        return tenantOrganizationService.findAllChildren(ids);
    }

    /**
     * 根据当前用户餐厅获取，餐厅拥有的库存库
     * @return
     */
    @GetMapping(value = "feign/findStockDataSource")
    public List<SimpDataSource> findStockDataSource(){
        return tenantOrganizationService.findStockDataSource();
    }

    /**
     * 根据当前库存库查询对应的食堂库，多个只取第一个
     * @return
     */
    @GetMapping(value = "feign/findMenuDataSource")
    public String findMenuDataSource(){
        return tenantOrganizationService.findMenuDataSource();
    }

    @GetMapping(value = "feign/findMenuId")
    public Long findMenuId() {
        return tenantOrganizationService.findMenuId();
    }

    /**
     * 获取当前租户最高级别租户信息
     * @return
     */
    @GetMapping(value = "feign/findTenantHighestChildren")
    public Set<Long> findTenantHighestChildren(){
        return tenantOrganizationService.findTenantHighestChildren();
    }

    @GetMapping(value = "feign/findTenantHighest")
    public Long findTenantHighest(){
        return tenantOrganizationService.findTenantHighest();
    }


    /**
     * 获取所有食堂
     * @return
     */
    @GetMapping(value = "feign/findAllCanteen")
    public List<FeignTenantOrganizationDto> findAllCanteen(){
        return tenantOrganizationService.findAllCanteen();
    }
}
