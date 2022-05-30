package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constants.TackOutPayType;
import com.iotinall.canteen.constants.TakeoutStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 外带外购订单
 *
 * @author WJH
 * @date 2019/11/2211:43
 */
@Data
@Entity
@Table(name = "mess_takeout_order")
@Accessors(chain = true)
@ToString(exclude = "messTakeoutOrderDetails")
@EqualsAndHashCode(exclude = "messTakeoutOrderDetails", callSuper = false)
@JsonIgnoreProperties(value = "messTakeoutOrderDetails")
public class MessTakeoutOrder extends BaseEntity {

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    /**
     * 支付方式
     */
    @Column(nullable = false)
    private TackOutPayType tackOutPayType;

    /**
     * 取货码
     */
    @Column(nullable = false)
    private Integer serialNumber; // 取货码

    /**
     * 订单产品
     */
    @OneToMany(mappedBy = "messTakeoutOrder", fetch = FetchType.EAGER)
    private Set<MessTakeoutOrderDetail> messTakeoutOrderDetails;

    /**
     * 总金额
     */
    @Column(nullable = false)
    private BigDecimal payAmount;

    @Column(nullable = false)
    private TakeoutStatus sourcingStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 餐厅租户ID
     */
    @Column(nullable = false, updatable = false)
    private Long tenantId;

}
