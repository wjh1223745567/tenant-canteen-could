package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenDisinfect;
import com.iotinall.canteen.entity.KitchenItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消毒管理 Repository
 *
 * @author xinbing
 * @date 2020-07-06 15:32:49
 */
public interface KitchenDisinfectRepository extends JpaRepositoryEnhance<KitchenDisinfect, Long>, JpaSpecificationExecutor<KitchenDisinfect> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    Boolean existsByItem(KitchenItem item);

    List<KitchenDisinfect> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    KitchenDisinfect findByRecordTimeAndCameraIdAndItem(LocalDateTime recordTime, Long cameraId, KitchenItem kitchenItem);
}