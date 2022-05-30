package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysReferenceCholesterol;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 营养档案 - 碳水化合物
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionReferenceCarbsRepository extends JpaRepositoryEnhance<SysReferenceCholesterol, Long>, JpaSpecificationExecutor<SysReferenceCholesterol> {

    /**
     * 获取推荐每日摄入碳水化合物 type + age +sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select rc.* from reference_cholesterol rc where rc.type = :type and (rc.age_start<=:age and :age<rc.age_end) and rc.sex = :sex ", nativeQuery = true)
    SysReferenceCholesterol queryCarbs(@Param("type") Integer type, @Param("age") Integer age, @Param("sex") Integer sex);

    /**
     * 获取推荐每日摄入碳水化合物 type  +sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select rc.* from reference_cholesterol rc where rc.type = :type and rc.sex = :sex ", nativeQuery = true)
    SysReferenceCholesterol queryCarbs(@Param("type") Integer type, @Param("sex") Integer sex);

}
