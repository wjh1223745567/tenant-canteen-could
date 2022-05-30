package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenSample;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 留样管理 Repository
 * @author xinbing
 * @date 2020-07-06 17:09:03
 */
public interface KitchenSampleRepository extends JpaRepositoryEnhance<KitchenSample, Long>, JpaSpecificationExecutor<KitchenSample> {

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    List<KitchenSample> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "from KitchenSample  s where s.img is not null")
    List<KitchenSample> findKitchenSampleList(Pageable pageable);
}