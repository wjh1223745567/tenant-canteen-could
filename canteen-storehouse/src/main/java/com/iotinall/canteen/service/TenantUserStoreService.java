package com.iotinall.canteen.service;

import com.iotinall.canteen.common.datasource.MultiDataSourceInit;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.tenant.TenantUser;
import com.iotinall.canteen.common.tenant.TenantUserRepository;
import com.iotinall.canteen.dto.tenantuser.TenantUserReq;
import com.iotinall.canteen.dto.tenantuser.TenantUserResp;
import com.iotinall.canteen.dto.tenantuser.TenantUserSelect;
import com.iotinall.canteen.dto.tenantuser.TenantUserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TenantUserStoreService {

    @Resource
    private TenantUserRepository tenantUserRepository;

    @Resource
    private MultiDataSourceInit multiDataSourceInit;

    public PageDTO<TenantUserResp> page(Pageable pageable){
        Page<TenantUserResp> result = tenantUserRepository.findAll(pageable).map(item -> {
            TenantUserResp tenantUserResp = new TenantUserResp()
                    .setCode(item.getCode())
                    .setName(item.getName())
                    .setSqlUrl(item.getSqlUrl())
                    .setSqlUsername(item.getSqlUsername());
            tenantUserResp.setId(item.getId());
            return tenantUserResp;
        });
        return PageUtil.toPageDTO(result);
    }

    /**
     * 下拉列表查询
     * @return
     */
    public List<TenantUserSelect> findSelect(){
        return this.tenantUserRepository.findAll(Sort.by(Sort.Order.desc("createTime"))).stream().map(item ->
                new TenantUserSelect()
                .setId(item.getId())
                .setName(item.getName())
                .setCode(item.getCode())
        ).collect(Collectors.toList());
    }

    /**
     * 添加，编辑接口
     * @param req
     */
    public void save(TenantUserReq req){
        TenantUser tenantUser = new TenantUser()
                .setName(req.getName())
                .setCode(req.getCode())
                .setSqlUrl(req.getSqlUrl())
                .setSqlUsername(req.getSqlUsername())
                .setSqlPassword(req.getSqlPassword());

        tenantUser.setId(req.getId());
        this.tenantUserRepository.save(tenantUser);
    }

    /**
     * 删除
     * @param ids
     */
    public void deleted(Set<Long> ids){
        List<TenantUser> tenantUsers = ids.stream().map(item -> {
            TenantUser tenantUser = new TenantUser();
            tenantUser.setId(item);
            return tenantUser;
        }).collect(Collectors.toList());
        this.tenantUserRepository.deleteInBatch(tenantUsers);
    }

    public TenantUserView findById(Long id){
        TenantUser tenantUser = this.tenantUserRepository.findById(id).orElseThrow(() -> new BizException("", "未找到数据"));
        TenantUserView tenantUserView = new TenantUserView()
                .setCode(tenantUser.getCode())
                .setName(tenantUser.getName())
                .setSqlUrl(tenantUser.getSqlUrl())
                .setSqlUsername(tenantUser.getSqlUsername())
                .setSqlPassword(tenantUser.getSqlPassword());
        tenantUserView.setId(tenantUser.getId());
        return tenantUserView;
    }

}
