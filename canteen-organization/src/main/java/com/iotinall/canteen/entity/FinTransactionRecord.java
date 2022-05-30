package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户消费记录
 *
 * @author xin-bing
 * @date 10/23/2019 15:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "fin_transaction_record")
public class FinTransactionRecord extends BaseEntity {
    @Column
    private String cardNo;

    @JoinColumn(name = "emp_id")
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private OrgEmployee employee;

    @JoinColumn(name = "org_id")
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Org org;

    /**
     * 金额
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * 付款类型，刷脸支付
     */
    @Column(nullable = false)
    private Integer payType;

    /**
     * 1 早餐 2. 午餐 4 晚餐 8 外带外购
     */
    private Integer eatType;

    /**
     * 0是消费的，1是后台创建的
     */
    @Column(nullable = false)
    private Integer typ;

    private LocalDateTime transactionTime;

    private String creator;

    private String deleter;

    /**
     * 状态 1-正常 2-作废
     */
    private Integer state;

    /**
     * 消费数据来源
     */
    private Integer source;

    /**
     * 关联同步记录ID  当消费数据来源于闸机通行系统
     */
    private Long consumeRecordId;

    /**
     * 网上商城订单ID
     */
    @Column(name = "order_id")
    private Long tackOutOrderId;
}
