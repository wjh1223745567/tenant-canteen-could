package com.iotinall.canteen.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(value = "canteen-organization", contextId = "org")
public interface FeignOrgService {

    @PostMapping(value = "/orgs/feign/getAllChildOrg")
    Set<Long> getAllChildOrg(@RequestBody List<Long> orgIds);

}
