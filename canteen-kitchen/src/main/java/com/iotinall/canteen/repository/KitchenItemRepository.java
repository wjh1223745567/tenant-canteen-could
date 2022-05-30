package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KitchenItemRepository extends JpaRepositoryEnhance<KitchenItem, Long>, JpaSpecificationExecutor<KitchenItem> {

    /**
     * 不查询已删除的
     * @param id
     * @return
     */
    @Query(value = "from KitchenItem where id = :id and deleted = false")
    KitchenItem findByIdIgnoreDeleted(@Param("id") Long id);

    @Query(value = "from KitchenItem where groupCode = :groupCode and deleted = false order by seq asc, id desc")
    List<KitchenItem> findByGroupCodeOrderBySeq(@Param("groupCode") String groupCode);

    KitchenItem findByGroupCodeAndName(String groupCode, String name);

}
