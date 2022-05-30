package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysReferenceStrength;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 营养档案-活动水平
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionReferenceStrengthRepository extends JpaRepositoryEnhance<SysReferenceStrength, Long>, JpaSpecificationExecutor<SysReferenceStrength> {
    /**
     * 获取活动水平 type + age +sex +level
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select rs.* from reference_strength rs where rs.type = :type and (rs.age_start<=:age and :age<rs.age_end) and rs.sex = :sex and rs.level = :level ", nativeQuery = true)
    SysReferenceStrength queryStrength(@Param("type") Integer type, @Param("age") Integer age, @Param("sex") Integer sex, @Param("level") Integer level);

    /**
     * 获取活动水平 type+weeks+sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select rs.* from reference_strength rs where rs.type = :type and (rs.age_start<=:weeks and :weeks<rs.age_end) and rs.sex = :sex", nativeQuery = true)
    SysReferenceStrength queryStrength(@Param("type") Integer type, @Param("weeks") Integer weeks, @Param("sex") Integer sex);

    /**
     * 获取活动水平 type + sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select rs.* from reference_strength rs where rs.type = :type and rs.sex = :sex ", nativeQuery = true)
    SysReferenceStrength queryStrength(@Param("type") Integer type, @Param("sex") Integer sex);
}
