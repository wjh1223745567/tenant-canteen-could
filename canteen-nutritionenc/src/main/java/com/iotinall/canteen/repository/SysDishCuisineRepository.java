package com.iotinall.canteen.repository;


import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysCuisine;
import com.iotinall.canteen.entity.SysDish;
import com.iotinall.canteen.entity.SysDishCuisine;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 菜品原料 Repository
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysDishCuisineRepository extends JpaRepositoryEnhance<SysDishCuisine, String>, JpaSpecificationExecutor<SysDishCuisine> {

    /**
     * 根据菜品获取菜品原材料
     *
     * @param dish
     * @return
     */
    List<SysDishCuisine> queryByDish(SysDish dish);


    SysDishCuisine queryByDishAndCuisine(SysDish dish, SysCuisine sysCuisine);

    /**
     * 删除菜谱原料
     */
    void deleteAllByDish(SysDish dish);
}