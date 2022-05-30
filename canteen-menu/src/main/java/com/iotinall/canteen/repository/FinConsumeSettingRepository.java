package com.iotinall.canteen.repository;

import com.iotinall.canteen.constants.MealTypeEnum;
import com.iotinall.canteen.entity.FinConsumeSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author WJH
 * @date 2019/11/2316:30
 */
public interface FinConsumeSettingRepository extends JpaRepository<FinConsumeSetting, Long>, JpaSpecificationExecutor<FinConsumeSetting> {

    @Query("select o from FinConsumeSetting o where o.beginTime <= ?1 and o.endTime >= ?1")
    FinConsumeSetting getByTime(String nowTime);

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    @Query(value = "delete from FinConsumeSetting where id in :ids")
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    FinConsumeSetting queryByMealType(MealTypeEnum mealType);

    FinConsumeSetting findByMealType(MealTypeEnum mealType);
}
