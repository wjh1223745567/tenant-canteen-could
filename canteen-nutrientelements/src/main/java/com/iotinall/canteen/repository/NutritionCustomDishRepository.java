package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.NutritionCustomDish;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 营养档案 ,自定义食物
 *
 * @author loki
 * @date 2020/04/10 15:07
 */
public interface NutritionCustomDishRepository extends JpaRepositoryEnhance<NutritionCustomDish, Long>, JpaSpecificationExecutor<NutritionCustomDish> {

    List<NutritionCustomDish> findAllByEmployeeId(Long employeeId);
}
