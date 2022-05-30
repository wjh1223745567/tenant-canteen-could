package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kitchen_food_additive_record")
public class KitchenFoodAdditiveRecord extends BaseEntity {
    private String img;

    @Column(nullable = false)
    private LocalDateTime recordTime;

    @Column(nullable = false, name = "product_id")
    private Long messProductId;

    /**
     * 冗余菜品名称，方便关键字查询
     */
    private String messProductName;

    @ManyToOne
    @JoinColumn(nullable = false, name = "item_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private KitchenItem item; // 添加剂

    @Column(nullable = false, columnDefinition = "decimal(8,2)")
    private BigDecimal dose;

    @Column(nullable = false, name = "duty_emp_id")
    private Long dutyEmpId;

    @Column(nullable = false, name = "auditor_id")
    private Long auditorId;

    @Column(nullable = false)
    private Integer state; // 状态

    private String comments; // 评价人
}
