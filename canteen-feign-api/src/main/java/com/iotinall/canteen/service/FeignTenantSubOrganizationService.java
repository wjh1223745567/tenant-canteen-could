package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.storehouse.FeignTenantSubOrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "canteen-storehouse", contextId = "tenant-sub-org")
public interface FeignTenantSubOrganizationService {
    /**
     * 获取就餐点
     */
    @GetMapping(value = "/tenant_sub_org_info/feign/findById/{id}")
    FeignTenantSubOrganizationDto findById(@PathVariable(value = "id") Long id);
}
