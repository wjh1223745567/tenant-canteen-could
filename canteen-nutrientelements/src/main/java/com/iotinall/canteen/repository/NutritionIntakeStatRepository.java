package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.NutritionIntakeStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 营养档案 ,自定义食物
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionIntakeStatRepository extends JpaRepositoryEnhance<NutritionIntakeStat, Long>, JpaSpecificationExecutor<NutritionIntakeStat> {
    /**
     * 获取某一天计划和摄入差值(能量/蛋白质/脂肪/碳水化合物/膳食纤维)
     *
     * @author loki
     * @date 2020/04/15 20:11
     */
    NutritionIntakeStat queryByEmployeeIdAndPlanDate(Long employeeId, LocalDate planDate);

    /**
     * 获取饮食摄入能量列表
     *
     * @author loki
     * @date 2020/04/15 20:11
     */
    @Query(value = "select nis.* from nutrition_intake_stat nis where nis.employee_id = :employeeId and nis.plan_date between :beginDate and :endDate order by nis.plan_date asc ", nativeQuery = true)
    List<NutritionIntakeStat> queryByEmployeeList(@Param("employeeId") Long employeeId, @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

    /**
     * 获取饮食摄入能量列表
     *
     * @author loki
     * @date 2020/04/15 20:11
     */
    Page<NutritionIntakeStat> queryByEmployeeId(Long employeeId, Pageable page);
}
