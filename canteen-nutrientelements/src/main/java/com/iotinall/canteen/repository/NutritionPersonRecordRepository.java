package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.NutritionPersonRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 营养档案 ,用户基本信息
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionPersonRecordRepository extends JpaRepositoryEnhance<NutritionPersonRecord, Long>, JpaSpecificationExecutor<NutritionPersonRecord> {

    /**
     * 根据用户信息查找用户营养档案
     *
     * @author loki
     * @date 2020/04/10 15:21
     */
    NutritionPersonRecord queryByEmployeeId(Long employeeId);
}
