package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MessTakeoutProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessTakeoutProductStockRepository extends JpaRepository<MessTakeoutProductStock, Long>, JpaSpecificationExecutor<MessTakeoutProductStock> {

    Integer countByProductTypeId(Long id);

    List<MessTakeoutProductStock> queryByState(Boolean state);

    @Modifying
    @Query(value = "update MessTakeoutProductStock p set p.state = :state where p.id = :id")
    int toggleProduct(@Param(value = "id") Long id, @Param(value = "state") Boolean state);


}