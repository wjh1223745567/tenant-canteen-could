package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.LightKitchenViolateRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 名厨亮灶违规图片记录 Repository
 *
 * @author loki
 * @date 2019-10-30 10:20:51
 */
public interface LightKitchenViolateRecordRepository extends JpaRepositoryEnhance<LightKitchenViolateRecord, Long>, JpaSpecificationExecutor<LightKitchenViolateRecord> {

}
