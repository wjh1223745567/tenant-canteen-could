package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FoodNote;
import com.iotinall.canteen.entity.FoodNoteCollectRecord;
import com.iotinall.canteen.entity.FoodNoteLikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FoodNoteLikeRecordRepository extends JpaRepository<FoodNoteLikeRecord, Long>, JpaSpecificationExecutor<FoodNoteLikeRecord> {

    FoodNoteLikeRecord findByUserIdAndFoodNote(Long userId , FoodNote foodNote);

    void deleteAllByFoodNote(FoodNote foodNote);

    Integer countAllByFoodNote(FoodNote foodNote);
}
