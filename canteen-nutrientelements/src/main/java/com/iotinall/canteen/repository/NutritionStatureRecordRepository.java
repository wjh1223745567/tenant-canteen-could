package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.NutritionStatureRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 营养档案 ,用户基本信息
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionStatureRecordRepository extends JpaRepositoryEnhance<NutritionStatureRecord, Long>, JpaSpecificationExecutor<NutritionStatureRecord> {

    /**
     * 查询身材记录
     *
     * @author loki
     * @date 2020/04/10 15:21
     */
    List<NutritionStatureRecord> queryByEmployeeIdAndRecordDate(Long employeeId, LocalDate date);

    /**
     * 根据类型查询最新指标参数
     * @param code
     * @param empId
     * @return
     */
    @Query(value = "select o.* from nutrition_stature_record o where o.code= ?1 and o.employee_id = ?2 order by o.record_date desc limit 0,1 ", nativeQuery = true)
    NutritionStatureRecord findByTypeMaxDate(String code, Long empId);

    /**
     * 查询身材记录
     *
     * @author loki
     * @date 2020/04/10 15:21
     */
    Page<NutritionStatureRecord> queryByEmployeeIdAndCodeOrderByRecordDateDesc(Long employeeId, String code, Pageable page);

    /**
     * 查询身材记录
     *
     * @author loki
     * @date 2020/04/15 20:11
     */
    @Query(value = "select nsr.* from nutrition_stature_record nsr " +
            "where nsr.employee_id = :employeeId " +
            "and nsr.code = :code " +
            "and nsr.record_date between :beginDate " +
            "and :endDate order by nsr.record_date asc", nativeQuery = true)
    List<NutritionStatureRecord> queryPersonStatureList(@Param("employeeId") Long employeeId,
                                                        @Param("code") String code,
                                                        @Param("beginDate") LocalDate beginDate,
                                                        @Param("endDate") LocalDate endDate);

}
