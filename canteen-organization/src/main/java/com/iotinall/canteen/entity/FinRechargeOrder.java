package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constant.OrderType;
import com.iotinall.canteen.constant.PayTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 用户充值订单
 * @author xin-bing
 * @date 10/23/2019 15:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
public class FinRechargeOrder extends BaseEntity {
    @Column(nullable = false, length = 32)
    private String orderNo; // 订单编号

    @Column(nullable = false, length = 128)
    private String transactionNo; // 第三方平台的交易单号

    @Column(nullable = false)
    private String cardNo; // 卡号

    @ManyToOne
    @JoinColumn(name = "emp_id", nullable = false)
    private OrgEmployee employee; // 所属员工

    private BigDecimal amount;

    @Enumerated
    @Column(nullable = false)
    private PayTypeEnum payType; // 支付类型

    @Column(nullable = false)
    private OrderType status; // 订单状态

}
