package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.constants.MealTypeEnum;
import com.iotinall.canteen.entity.MessDailyMenuItem;
import com.iotinall.canteen.entity.MessProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MessDailyMenuItemRepository extends JpaRepositoryEnhance<MessDailyMenuItem, Long>, JpaSpecificationExecutor<MessDailyMenuItem> {

    MessDailyMenuItem queryByMealTypeAndProductAndMenuId(MealTypeEnum mealType, MessProduct product, Long menuId);

    List<MessDailyMenuItem> findByMenuIdAndMealType(Long menuId, MealTypeEnum mealType);

    MessDailyMenuItem findByMenuIdAndProductAndMealType(Long menuId, MessProduct product, MealTypeEnum mealType);

    MessDailyMenuItem findByMenuIdAndProduct(Long menuId, MessProduct messProduct);
}
