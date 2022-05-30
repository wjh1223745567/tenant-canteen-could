package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysReferenceProtein;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 营养档案 - 能量
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionReferenceProteinRepository extends JpaRepositoryEnhance<SysReferenceProtein, Long>, JpaSpecificationExecutor<SysReferenceProtein> {

    /**
     * 获取推荐每日摄入蛋白质 type + age +sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select re.* from reference_protein re where re.type = :type and (re.age_start<=:age and :age<re.age_end) and re.sex = :sex ", nativeQuery = true)
    SysReferenceProtein queryProtein(@Param("type") Integer type, @Param("age") Integer age, @Param("sex") Integer sex);

    /**
     * 获取推荐每日摄入蛋白质 type  +sex
     *
     * @author loki
     * @date 2020/04/14 20:29
     */
    @Query(value = "select re.* from reference_protein re where re.type = :type and re.sex = :sex ", nativeQuery = true)
    SysReferenceProtein queryProtein(@Param("type") Integer type, @Param("sex") Integer sex);

}
