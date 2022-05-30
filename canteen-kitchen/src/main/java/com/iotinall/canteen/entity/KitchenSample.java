package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constant.MealTypeEnum;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kitchen_sample")
public class KitchenSample extends BaseEntity {
    private String img;

    @Column(nullable = false)
    private LocalDateTime recordTime; // 检查时间

    @Column(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private MealTypeEnum mealType; // 餐次类型

    @ManyToOne
    @JoinColumn(name = "item_id")
    private KitchenItem item;

    /**
     * 责任人,多个以逗号拼接
     */
    private String dutyEmployees;

    @Column(name = "state")
    private Integer state;

    private String comments; // 检查备注

    private String requirements;//留样要求
}
