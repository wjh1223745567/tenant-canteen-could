package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MessDailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * mess_daily_menu Repository
 *
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
public interface MessDailyMenuRepository extends JpaRepository<MessDailyMenu, Long>, JpaSpecificationExecutor<MessDailyMenu> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    @Query(value = "delete from MessDailyMenu where id in :ids")
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    /**
     * 根据menuDate查找
     *
     * @param menuDate
     * @return
     */
    MessDailyMenu findByMenuDate(LocalDate menuDate);

    List<MessDailyMenu> queryByMenuDateBetween(LocalDate beginDate, LocalDate endDate);

    MessDailyMenu findFirstByMenuDate(LocalDate menuDate);

    @Query(value = "select m.product_id from mess_daily_menu_item m where m.menu_id = :menuId and m.meal_type=:mealType", nativeQuery = true)
    Set<Long> queryMessDailyMenuItem(@Param("menuId") Long menuId, @Param("mealType") Integer mealType);

    @Query(value = "select m.product_id from mess_daily_menu_item m where m.menu_id = :menuId", nativeQuery = true)
    Set<Long> queryDailyMenuItem(@Param("menuId") Long menuId);
}