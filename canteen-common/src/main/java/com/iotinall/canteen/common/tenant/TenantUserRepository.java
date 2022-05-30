package com.iotinall.canteen.common.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantUserRepository extends JpaRepository<TenantUser, Long>, JpaSpecificationExecutor<TenantUser> {

    @Query("from TenantUser o where o.code = ?1")
    TenantUser findBySource(String dataSource);

}
