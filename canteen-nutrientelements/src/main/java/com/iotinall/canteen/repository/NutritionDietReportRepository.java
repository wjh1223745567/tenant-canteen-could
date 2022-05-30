package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.NutritionDietReport;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;

/**
 * 营养档案 ,用户基本信息
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionDietReportRepository extends JpaRepositoryEnhance<NutritionDietReport, Long>, JpaSpecificationExecutor<NutritionDietReport> {
    /**
     * 获取某日的饮食记录
     *
     * @author loki
     * @date 2020/04/10 20:13
     */
    NutritionDietReport queryByEmployeeIdAndReportDate(Long employeeId, LocalDate date);
}
