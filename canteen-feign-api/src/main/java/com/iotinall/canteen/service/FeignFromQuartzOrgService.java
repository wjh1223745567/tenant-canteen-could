package com.iotinall.canteen.service;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.security.SecurityUserDetails;
import com.iotinall.canteen.dto.organization.FeignLoginReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "canteen-organization", contextId = "quartz-organ")
public interface FeignFromQuartzOrgService {

    @PostMapping(value = "/auth/login")
    ResultDTO<SecurityUserDetails> login(@RequestBody FeignLoginReq loginReq);

}
