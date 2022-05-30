package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FoodNote;
import com.iotinall.canteen.entity.FoodNoteCommentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface FoodNoteCommentRecordRepository extends JpaRepository<FoodNoteCommentRecord, Long>, JpaSpecificationExecutor<FoodNoteCommentRecord> {

    @Query(value = "select id from FoodNoteCommentRecord where lastId =:id")
    List<Long> findAllByLastId(Long id);

    @Query(value = "select id from FoodNoteCommentRecord where foodNote =:foodNote")
    List<Long> findAllByFoodNote(FoodNote foodNote);

    Integer countAllByFoodNote(FoodNote foodNote);

    List<FoodNoteCommentRecord> findAllByFoodNoteAndAndLastId(FoodNote foodNote,Long lastId);
}
