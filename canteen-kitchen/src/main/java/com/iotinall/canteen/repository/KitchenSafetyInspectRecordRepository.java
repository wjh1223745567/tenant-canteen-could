package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenSafetyInspectRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消防安全 Repository
 * @author xinbing
 * @date 2020-07-10 11:10:12
 */
public interface KitchenSafetyInspectRecordRepository extends JpaRepositoryEnhance<KitchenSafetyInspectRecord, Long>, JpaSpecificationExecutor<KitchenSafetyInspectRecord> {

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    Boolean existsByItem(KitchenItem item);

    List<KitchenSafetyInspectRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}