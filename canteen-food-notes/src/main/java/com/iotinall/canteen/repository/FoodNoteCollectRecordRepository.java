package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FoodNote;
import com.iotinall.canteen.entity.FoodNoteCollectRecord;
import com.iotinall.canteen.entity.FoodNoteFollowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FoodNoteCollectRecordRepository extends JpaRepository<FoodNoteCollectRecord, Long>, JpaSpecificationExecutor<FoodNoteCollectRecord> {

    FoodNoteCollectRecord findByUserIdAndFoodNote(Long userId , FoodNote foodNote);

    void deleteAllByFoodNote(FoodNote foodNote);

    Integer countAllByFoodNote(FoodNote foodNote);

}
