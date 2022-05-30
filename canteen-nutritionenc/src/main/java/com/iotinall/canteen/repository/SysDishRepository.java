package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysCuisine;
import com.iotinall.canteen.entity.SysDish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜品 Repository
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysDishRepository extends JpaRepositoryEnhance<SysDish, String>, JpaSpecificationExecutor<SysDish> {
    /**
     * 根据菜品名称获取菜品列表
     *
     * @param name
     * @return
     */
    @Query(value = "select d.* from dish d where  if(:name!='',d.name like concat('%',:name,'%'),1=1) order by d.created_time limit 10", nativeQuery = true)
    List<SysDish> queryTop10DishByName(@Param(value = "name") String name);

    @Query(value = "select d.* from dish_cuisine dc  left outer join dish d on(dc.dish_id=d.id) where dc.cuisine_id =:cuisineId and if(:name!='',d.name like concat('%',:name,'%'),1=1)", nativeQuery = true, countProjection = "1")
    Page<SysDish> queryNutritionConditionDishPage(@Param("name") String name, @Param("cuisineId") String cuisineId, Pageable page);

    List<SysDish> queryByCuisinesIn(@Param("cuisines") Set<SysCuisine> cuisines);

}