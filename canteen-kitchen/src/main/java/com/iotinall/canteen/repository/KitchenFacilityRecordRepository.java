package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenFacilityRecord;
import com.iotinall.canteen.entity.KitchenItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * kitchen_facility_record Repository
 *
 * @author xinbing
 * @date 2020-07-10 11:32:53
 */
public interface KitchenFacilityRecordRepository extends JpaRepositoryEnhance<KitchenFacilityRecord, Long>, JpaSpecificationExecutor<KitchenFacilityRecord> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    Boolean existsByItem(KitchenItem item);

    List<KitchenFacilityRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    KitchenFacilityRecord findByRecordTimeAndCameraIdAndItem(LocalDateTime recordTime, Long cameraId, KitchenItem kitchenItem);
}