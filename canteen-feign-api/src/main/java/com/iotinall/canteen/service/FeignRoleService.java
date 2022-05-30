package com.iotinall.canteen.service;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.role.FeignRoleAddReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "canteen-organization", contextId = "role")
public interface FeignRoleService {

    /**
     * 新建立租户组织生成默认的角色
     *
     * @author loki
     * @date 2021/7/16 15:43
     **/
    @PostMapping(value = "sys/roles/feign/createTenantDefaultRole")
    ResultDTO createTenantDefaultRole(@Valid @RequestBody FeignRoleAddReq req);
}
