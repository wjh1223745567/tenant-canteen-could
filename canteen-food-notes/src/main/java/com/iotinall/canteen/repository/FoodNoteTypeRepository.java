package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FoodNoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FoodNoteTypeRepository extends JpaRepository<FoodNoteType, Long>, JpaSpecificationExecutor<FoodNoteType> {

    FoodNoteType findByName(String name);

    List<FoodNoteType> findAllByStatus(Boolean status);
}
