package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MessProductCuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessProductCuisineRepository extends JpaRepository<MessProductCuisine, Long>, JpaSpecificationExecutor<MessProductCuisine> {
}
