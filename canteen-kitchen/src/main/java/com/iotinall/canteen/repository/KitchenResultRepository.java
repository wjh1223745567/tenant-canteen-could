package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenResult;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KitchenResultRepository extends JpaRepositoryEnhance<KitchenResult, Long>, JpaSpecificationExecutor<KitchenResult> {

    KitchenResult findByRecordIdAndItemType(Long recordId, String itemType);

    @Modifying
    int deleteByRecordIdAndItemType(Long recordId, String itemType);

    @Query(value = "delete from KitchenResult where recordId in :recordId and itemType = :itemType")
    @Modifying
    int deleteByRecordIdInAndItemType(@Param("recordId")Long[] recordId, @Param("itemType")String itemType);
}
