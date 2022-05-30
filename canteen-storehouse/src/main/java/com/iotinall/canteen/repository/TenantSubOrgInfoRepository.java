package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.TenantSubOrgInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TenantSubOrgInfoRepository extends JpaRepository<TenantSubOrgInfo, Long>, JpaSpecificationExecutor<TenantSubOrgInfo> {

    @Query("from TenantSubOrgInfo o where o.tenantOrgId = ?1")
    List<TenantSubOrgInfo> findByTenantOrgId(Long tenantOrgId);

}
