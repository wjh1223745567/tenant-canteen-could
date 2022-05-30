package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MessTakeoutProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商品类型
 */
public interface MessTakeoutProductTypeRepository extends JpaRepository<MessTakeoutProductType, Long>, JpaSpecificationExecutor<MessTakeoutProductType> {


    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    List<MessTakeoutProductType> findByNameAndTenantId(String name,Long tenantId);

    /**
     * 商品类型树
     *
     * @return
     */
    @Query(value = "from MessTakeoutProductType p where p.parent is null and p.tenantId = ?1 order by p.id asc")
    List<MessTakeoutProductType> findTree(Long tenantId);

    /**
     * 查询所有子节点
     *
     * @param takeoutProductType
     * @return
     */
    List<MessTakeoutProductType> findAllByParent(MessTakeoutProductType takeoutProductType);

    List<MessTakeoutProductType> findAllByParentIsNullAndTenantId(Long tenantId);
}
