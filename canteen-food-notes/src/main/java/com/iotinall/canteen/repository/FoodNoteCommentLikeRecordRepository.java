package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FoodNoteCommentLikeRecord;
import com.iotinall.canteen.entity.FoodNoteCommentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FoodNoteCommentLikeRecordRepository extends JpaRepository<FoodNoteCommentLikeRecord, Long>, JpaSpecificationExecutor<FoodNoteCommentLikeRecord> {

    FoodNoteCommentLikeRecord findByUserIdAndAndFoodNoteCommentRecord(Long userId, FoodNoteCommentRecord foodNoteCommentRecord);

    void deleteAllByFoodNoteCommentRecord(FoodNoteCommentRecord foodNoteCommentRecord);

    Integer countAllByFoodNoteCommentRecord(FoodNoteCommentRecord foodNoteCommentRecord);
}
