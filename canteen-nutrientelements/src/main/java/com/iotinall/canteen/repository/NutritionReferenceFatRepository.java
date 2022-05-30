package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysReferenceFat;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 营养档案 - 脂肪
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionReferenceFatRepository extends JpaRepositoryEnhance<SysReferenceFat, Long>, JpaSpecificationExecutor<SysReferenceFat> {

    /**
     * 获取推荐每日摄入脂肪 type + age +sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select rf.* from reference_fat rf where rf.type = :type and (rf.age_start<=:age and :age <rf.age_end) and rf.sex = :sex ", nativeQuery = true)
    SysReferenceFat queryFat(@Param("type") Integer type, @Param("age") Integer age, @Param("sex") Integer sex);

    /**
     * 获取推荐每日摄入脂肪 type  +sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select rf.* from reference_fat rf where rf.type = :type and rf.sex = :sex ", nativeQuery = true)
    SysReferenceFat queryFat(@Param("type") Integer type, @Param("sex") Integer sex);

}
