package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.TenantOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TenantOrganizationRepository extends JpaRepository<TenantOrganization, Long>, JpaSpecificationExecutor<TenantOrganization> {

    @Query(value = "SELECT O.ID, MIN(O.LEVEL) FROM TENANT_ORGANIZATION O WHERE O.ID IN (?1) GROUP BY O.ID ", nativeQuery = true)
    Set<Long> findTenantHighestChildren(Set<Long> ids);

    /**
     * 根据数据源查询食堂租户
     * @param dataSource
     * @return
     */
    @Query("from TenantOrganization o where o.dataSourceKey = ?1 and o.type = 1")
    List<TenantOrganization> findByDataSourceAndType(String dataSource);

}
