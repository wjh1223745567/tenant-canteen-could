package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.WxMessageContentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WxMessageContenttConfigRepository extends JpaRepository<WxMessageContentConfig, Long>, JpaSpecificationExecutor<WxMessageContentConfig> {

    WxMessageContentConfig findByType(Integer type);

}
