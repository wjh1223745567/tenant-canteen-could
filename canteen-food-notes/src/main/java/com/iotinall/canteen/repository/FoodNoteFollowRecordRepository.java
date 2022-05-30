package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FoodNoteFollowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodNoteFollowRecordRepository extends JpaRepository<FoodNoteFollowRecord, Long>, JpaSpecificationExecutor<FoodNoteFollowRecord> {

    FoodNoteFollowRecord findByAuthorIdAndUserId(Long author, Long userId);

    void deleteAllByAuthorIdAndUserId(Long authorId, Long userId);

    @Query(value = "select authorId from FoodNoteFollowRecord where userId =:userId")
    List<Long> findAllByUserId(Long userId);
}
