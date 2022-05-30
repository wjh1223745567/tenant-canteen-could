package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.constants.AreaType;
import com.iotinall.canteen.entity.LiveInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author bingo
 * @date 1/10/2020 20:17
 */
public interface LiveInfoRepository extends JpaRepositoryEnhance<LiveInfo, Long>, JpaSpecificationExecutor<LiveInfo> {
    List<LiveInfo> findByAreaType(AreaType areaType);
}
