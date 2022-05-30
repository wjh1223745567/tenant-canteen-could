package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenBrightRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

/**
 * 明厨亮灶
 *
 * @author loki
 * @date 2021/6/24 11:54
 **/
public interface KitchenBrightRepository extends JpaRepositoryEnhance<KitchenBrightRecord, Long>, JpaSpecificationExecutor<KitchenBrightRecord> {
    /**
     * 统计违规人员
     */
    @Query(value = "select count(record.id) " +
            " from kitchen_bright_record record " +
            " where date_format(record.detect_time,'%Y-%m-%d')=:date",
            nativeQuery = true)
    Integer countViolationNumber(LocalDate date);
}
