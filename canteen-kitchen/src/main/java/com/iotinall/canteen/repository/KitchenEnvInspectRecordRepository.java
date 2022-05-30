package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenEnvInspectRecord;
import com.iotinall.canteen.entity.KitchenItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 环境卫生 Repository
 * @author xinbing
 * @date 2020-07-10 13:48:40
 */
public interface KitchenEnvInspectRecordRepository extends JpaRepositoryEnhance<KitchenEnvInspectRecord, Long>, JpaSpecificationExecutor<KitchenEnvInspectRecord> {

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    Boolean existsByItem(KitchenItem item);

    List<KitchenEnvInspectRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    KitchenEnvInspectRecord findByRecordTimeAndCameraIdAndItem(LocalDateTime recordTime, Long cameraId, KitchenItem item);

}