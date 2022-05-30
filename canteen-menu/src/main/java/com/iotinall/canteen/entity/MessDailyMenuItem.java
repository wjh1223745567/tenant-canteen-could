package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constants.MealTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * 每日菜谱清单
 *
 * @author xin-bing
 * @date 10/22/2019 16:37
 */
@EqualsAndHashCode(callSuper = true, exclude = {"product"})
@Data
@Entity
@Table(name = "mess_daily_menu_item", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_menu_id", columnList = "menuId")
})
public class MessDailyMenuItem extends BaseEntity {

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "product_id", nullable = false)
    private MessProduct product;

    /**
     * 厨师
     */
    @Column(name = "cook_id")
    private Long cookId;

    /**
     * 冗余厨师名称
     */
    private String cookName;

    @Enumerated
    @Column(nullable = false)
    private MealTypeEnum mealType;

    @Column(nullable = false)
    private Long menuId;

    /**
     * 排序
     */
    private Integer seq;
}
