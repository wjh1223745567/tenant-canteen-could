package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.NutritionDietRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

/**
 * 营养档案 ,用户基本信息
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionDietRecordRepository extends JpaRepositoryEnhance<NutritionDietRecord, Long>, JpaSpecificationExecutor<NutritionDietRecord> {
    /**
     * 获取饮食记录
     *
     * @author loki
     * @date 2020/04/10 20:13
     */
    List<NutritionDietRecord> queryByEmployeeIdAndRecordDate(Long employeeId, LocalDate date);
}
