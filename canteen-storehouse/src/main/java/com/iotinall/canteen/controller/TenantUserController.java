package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.tenantuser.TenantUserReq;
import com.iotinall.canteen.service.TenantUserStoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Set;

/**
 * 多数据源接口
 */
@RestController
@RequestMapping(value = "tenant_user")
public class TenantUserController {

    @Resource
    private TenantUserStoreService tenantUserStoreService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> page(@PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResultDTO.success(tenantUserStoreService.page(pageable));
    }

    @GetMapping(value = "find_select")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> findSelect(){
        return ResultDTO.success(tenantUserStoreService.findSelect());
    }

    @GetMapping(value = "find_by_id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> findById(@PathVariable(value = "id") Long id){
        return ResultDTO.success(tenantUserStoreService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> add(@Valid @RequestBody TenantUserReq req){
        req.setId(req.getId());
        tenantUserStoreService.save(req);
        return ResultDTO.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> edit(@Valid @RequestBody TenantUserReq req){
        if(req.getId() == null){
            throw new BizException("", "ID不能为空");
        }
        tenantUserStoreService.save(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> deleted(@RequestParam(value = "batchId") Set<Long> ids){
        tenantUserStoreService.deleted(ids);
        return ResultDTO.success();
    }
}
