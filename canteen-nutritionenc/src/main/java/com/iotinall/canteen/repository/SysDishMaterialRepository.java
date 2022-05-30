package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.SysDish;
import com.iotinall.canteen.entity.SysDishMaterial;
import com.iotinall.canteen.entity.SysMaterial;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 菜品原料 Repository
 *
 * @author loki
 * @date 2020/03/25 14:47
 */
public interface SysDishMaterialRepository extends JpaRepositoryEnhance<SysDishMaterial, String>, JpaSpecificationExecutor<SysDishMaterial> {

    /**
     * 根据菜品获取菜品原材料
     *
     * @param dish
     * @return
     */
    List<SysDishMaterial> queryByDish(SysDish dish);

    /**
     * 根据菜品获取菜品原材料
     *
     * @param material
     * @return
     */
    List<SysDishMaterial> queryByMaterial(SysMaterial material);

    /**
     * 删除菜谱原料
     */
    void deleteAllByDish(SysDish dish);
}