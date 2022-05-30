package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysCuisine;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * 菜品类别
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysCuisineRepository extends JpaRepositoryEnhance<SysCuisine, String>, JpaSpecificationExecutor<SysCuisine> {
    /**
     * 根据分类code 获取分类
     *
     * @author loki
     * @date 2020/04/13 20:13
     */
    SysCuisine queryByCode(@Param("code") String code);

    /**
     * 获取所有的子分类
     *
     * @author loki
     * @date 2020/04/22 11:39
     */
    Set<SysCuisine> queryByMasterIdIn(@Param("ids") Set<String> ids);

    /**
     * 获取所有的子分类
     *
     * @author loki
     * @date 2020/04/22 11:39
     */
    Set<SysCuisine> queryByMasterIdOrderBySeqAsc(@Param("id") String id);
}