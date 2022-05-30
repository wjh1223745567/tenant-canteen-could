package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.Mess;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author bingo
 * @date 1/11/2020 15:16
 */
public interface MessRepository extends JpaRepositoryEnhance<Mess, Long>, JpaSpecificationExecutor<Mess> {
    Mess findFirstBy();

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    @Modifying
    @Query(value = "delete from Mess where id in :ids")
    int deleteByIdIn(@Param(value = "ids") Long[] ids);
}
