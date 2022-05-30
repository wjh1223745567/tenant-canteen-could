package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constants.MealTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 消费设置表
 * @author xin-bing
 * @date 10/23/2019 15:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "fin_consume_setting")
public class FinConsumeSetting extends BaseEntity {
    @Column(nullable = false, length = 64)
    private String name; // 名称

    @Enumerated
    @Column(nullable = false)
    private MealTypeEnum mealType; // 餐饮类型

    @Column(nullable = false)
    private String beginTime; // 开始时间，格式HH:mm

    @Column(nullable = false)
    private String endTime; // 结束时间，格式HH:mm
}
