package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FoodNote;
import com.iotinall.canteen.entity.FoodNoteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FoodNoteRepository extends JpaRepository<FoodNote, Long>, JpaSpecificationExecutor<FoodNote> {

//    Boolean existsByFoodNoteTypeAndStatus(FoodNoteType type, Boolean status);

    Boolean existsByFoodNoteType(FoodNoteType type);

    int countByFoodNoteTypeAndStatus(FoodNoteType type, Boolean status);

    Page<FoodNote> findAllByStatusAndAuthorIdIn(Boolean status, List<Long> authorIds, Pageable pageable);

    Page<FoodNote> findAllByStatusAndAuthorId(Boolean status, Long authorId, Pageable pageable);
}
