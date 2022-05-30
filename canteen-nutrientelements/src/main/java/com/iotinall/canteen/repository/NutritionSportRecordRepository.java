package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.NutritionSportRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 运动记录持久化类
 *
 * @author loki
 * @date 2020/04/14 10:07
 */
public interface NutritionSportRecordRepository extends JpaRepositoryEnhance<NutritionSportRecord, Long>, JpaSpecificationExecutor<NutritionSportRecord> {

    /**
     * 查询运动记录
     *
     * @author loki
     * @date 2020/04/14 10:07
     */
    List<NutritionSportRecord> queryByEmployeeIdAndSportDate(Long employeeId, LocalDate sportDate);

    /**
     * 查询运动记录列表
     *
     * @author loki
     * @date 2020/04/15 20:11
     */
    @Query(value = "select nsr.* from nutrition_sport_record nsr where nsr.employee_id = :employeeId and nsr.sport_date between :beginDate and :endDate order by nsr.sport_date asc", nativeQuery = true)
    List<NutritionSportRecord> queryByEmployeeList(@Param("employeeId") Long employeeId, @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

    /**
     * 查询运动记录列表
     *
     * @author loki
     * @date 2020/04/14 10:07
     */
    Page<NutritionSportRecord> queryByEmployeeIdOrderBySportDateDesc(Long employeeId, Pageable page);
}
