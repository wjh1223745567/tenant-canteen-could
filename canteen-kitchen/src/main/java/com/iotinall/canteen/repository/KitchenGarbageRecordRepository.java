package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenGarbageRecord;
import com.iotinall.canteen.entity.KitchenItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 餐厨垃圾 Repository
 * @author xinbing
 * @date 2020-07-10 11:46:09
 */
public interface KitchenGarbageRecordRepository extends JpaRepositoryEnhance<KitchenGarbageRecord, Long>, JpaSpecificationExecutor<KitchenGarbageRecord> {

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    List<KitchenGarbageRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    KitchenGarbageRecord findByRecordTimeAndCameraIdAndItem(LocalDateTime recordTime, Long cameraId, KitchenItem kitchenItem);
}