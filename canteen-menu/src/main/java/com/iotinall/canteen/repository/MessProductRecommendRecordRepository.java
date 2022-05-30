package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.MessProductRecommendRecord;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

public interface MessProductRecommendRecordRepository extends JpaRepositoryEnhance<MessProductRecommendRecord, Long> {

    MessProductRecommendRecord findByDateAndProductId(String date, Long productId);

    @Lock(value= LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "from MessProductRecommendRecord where date = :date and productId = :productId")
    MessProductRecommendRecord findByDateAndProductIdForUpdate(@Param(value = "date") String date,@Param(value = "productId") Long productId);

    @Query(value = "update MessProductRecommendRecord set recommendCount = :count, empList = :empList WHERE id = :id")
    @Modifying
    void updateRecommendEmp(@Param(value="count") int count, @Param(value="empList") String empList, @Param(value="id") Long id);
}
