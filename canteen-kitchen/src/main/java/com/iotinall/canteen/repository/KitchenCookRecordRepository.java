package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.dto.cookrecord.CommentsDTO;
import com.iotinall.canteen.entity.KitchenCookRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface KitchenCookRecordRepository extends JpaRepositoryEnhance<KitchenCookRecord, Long>, JpaSpecificationExecutor<KitchenCookRecord> {

    @Query(value = "SELECT\n" +
            "mc.product_id," +
            "sum( CASE WHEN mc.score = 5 THEN 1 ELSE 0 END ) as fiveStar,\n" +
            "sum( CASE WHEN mc.score = 4 THEN 1 ELSE 0 END ) as fourStar,\n" +
            "sum( CASE WHEN mc.score = 3 THEN 1 ELSE 0 END ) as threeStar,\n" +
            "sum( CASE WHEN mc.score = 2 THEN 1 ELSE 0 END ) as twoStar,\n" +
            "sum( CASE WHEN mc.score = 1 THEN 1 ELSE 0 END ) as oneStar\n" +
            "FROM mess_product_comment mc " +
            "WHERE mc.product_id = :id and mc.create_time >= :begin and mc.create_time <= :end", nativeQuery = true)
    CommentsDTO findByProductIdAndTime(@Param("id") Long id, @Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);


    List<KitchenCookRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
