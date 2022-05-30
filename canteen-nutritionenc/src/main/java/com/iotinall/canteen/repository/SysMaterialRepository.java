package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysMaterial;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 菜品 Repository
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysMaterialRepository extends JpaRepositoryEnhance<SysMaterial, String>, JpaSpecificationExecutor<SysMaterial> {
    /**
     * 根据材料名称获取材料列表
     *
     * @param name
     * @return
     */
    @Query(value = "select d.* from material d where  if(:name!='',d.name like concat('%',:name,'%'),1=1) order by d.created_time limit 10", nativeQuery = true)
    List<SysMaterial> queryTop10MaterialByName(@Param(value = "name") String name);
}