package com.iotinall.canteen.service;

import com.iotinall.canteen.common.protocol.SimpDataSource;
import com.iotinall.canteen.dto.storehouse.FeignTenantOrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(value = "canteen-storehouse", contextId = "tenant-org")
public interface FeignTenantOrganizationService {

    @GetMapping(value = "/tenant_organization/feign/findByIds")
    Map<Long, FeignTenantOrganizationDto> findByIds(@RequestBody Set<Long> ids);

    @GetMapping(value = "/tenant_organization/feign/findAll")
    Map<Long, FeignTenantOrganizationDto> findAll();

    @PostMapping(value = "/tenant_organization/feign/findAllChildren")
    Set<Long> findAllChildren(@RequestBody Set<Long> ids);

    /**
     * 获取当前餐厅使用的库存库
     * @return
     */
    @GetMapping(value = "/tenant_organization/feign/findStockDataSource")
    List<SimpDataSource> findStockDataSource();

    /**
     * 根据当前库存库，查询对应的食堂库
     * @return
     */
    @GetMapping(value = "/tenant_organization/feign/findMenuDataSource")
    String findMenuDataSource();

    /**
     * 获取当前餐厅ID
     * @return
     */
    @GetMapping(value = "/tenant_organization/feign/findMenuId")
    Long findMenuId();

    /**
     * 获取当前部门最高级别租户信息的子租户信息
     * @return
     */
    @GetMapping(value = "/tenant_organization/feign/findTenantHighestChildren")
    Set<Long> findTenantHighestChildren();

    /**
     * 获取当前部门最高级别租户信息。多个取第一个
     * @return
     */
    @GetMapping(value = "/tenant_organization/feign/findTenantHighest")
    Long findTenantHighest();

    /**
     * 获取所有餐厅
     * @return
     */
    @GetMapping(value = "/tenant_organization/feign/findAllCanteen")
    List<FeignTenantOrganizationDto> findAllCanteen();

}
