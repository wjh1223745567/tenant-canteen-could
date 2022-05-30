package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenFoodAdditiveRecord;
import com.iotinall.canteen.entity.KitchenItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 添加剂记录 Repository
 * @author xinbing
 * @date 2020-07-10 14:24:45
 */
public interface KitchenFoodAdditiveRecordRepository extends JpaRepositoryEnhance<KitchenFoodAdditiveRecord, Long>, JpaSpecificationExecutor<KitchenFoodAdditiveRecord> {

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    Boolean existsByItem(KitchenItem item);

    List<KitchenFoodAdditiveRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}