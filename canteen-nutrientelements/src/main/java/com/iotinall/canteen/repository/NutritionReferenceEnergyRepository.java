package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysReferenceEnergy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 营养档案 - 能量
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionReferenceEnergyRepository extends JpaRepositoryEnhance<SysReferenceEnergy, Long>, JpaSpecificationExecutor<SysReferenceEnergy> {

    /**
     * 获取活动水平 type + age +sex +level
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select re.* from reference_energy re where re.type = :type and (re.age_start<=:age and :age<re.age_end) and re.sex = :sex and re.strength_level = :level ", nativeQuery = true)
    SysReferenceEnergy queryEnergy(@Param("type") Integer type, @Param("age") Integer age, @Param("sex") Integer sex, @Param("level") Integer level);

    /**
     * 获取活动水平 type+weeks+sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select re.* from reference_energy re where re.type = :type and (re.age_start<=:weeks and :weeks<re.age_end) and re.sex = :sex", nativeQuery = true)
    SysReferenceEnergy queryEnergy(@Param("type") Integer type, @Param("weeks") Integer weeks, @Param("sex") Integer sex);

    /**
     * 获取活动水平 type + level
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select re.* from reference_energy re where re.type = :type and re.strength_level = :level ", nativeQuery = true)
    SysReferenceEnergy queryEnergy(@Param("type") Integer type, @Param("level") Integer level);
}
