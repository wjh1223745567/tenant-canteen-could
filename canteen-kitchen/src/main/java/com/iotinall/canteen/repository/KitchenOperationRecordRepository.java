package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenOperationRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * kitchen_operation_record Repository
 *
 * @author xinbing
 * @date 2020-07-09 15:26:01
 */
public interface KitchenOperationRecordRepository extends JpaRepositoryEnhance<KitchenOperationRecord, Long>, JpaSpecificationExecutor<KitchenOperationRecord> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    Boolean existsByItem(KitchenItem item);

    /**
     * 查询所有
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<KitchenOperationRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}