package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MessTakeoutProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessTakeoutProductInfoRepository extends JpaRepository<MessTakeoutProductInfo, Long>, JpaSpecificationExecutor<MessTakeoutProductInfo> {
}
