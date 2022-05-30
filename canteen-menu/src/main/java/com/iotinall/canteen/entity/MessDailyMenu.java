package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 餐厅每日菜谱
 *
 * @author xin-bing
 * @date 10/16/2019 11:23
 */
@Data
@EqualsAndHashCode(exclude = "menuItems", callSuper = false)
@Entity
@Table(name = "mess_daily_menu", indexes = {
        @Index(name = "idx_menu_date", columnList = "menuDate")
})
public class MessDailyMenu extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true)
    private LocalDate menuDate; // 菜谱日期

    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    @OrderBy("mealType asc,seq asc")
    private List<MessDailyMenuItem> menuItems;
}
