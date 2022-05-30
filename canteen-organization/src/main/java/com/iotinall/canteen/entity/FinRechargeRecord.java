package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constant.PayTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值记录
 * 和充值订单不是一回事，充值订单成功后会生成一条充值记录
 *
 * @author xin-bing
 * @date 10/23/2019 16:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "fin_recharge_record")
@Accessors(chain = true)
public class FinRechargeRecord extends BaseEntity {

    @Column
    private String cardNo;

    /**
     * 金额
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * 支付类型
     */
    @Enumerated
    @Column(nullable = false)
    private PayTypeEnum payType;

    /**
     * 充值时间
     */
    @Column(nullable = false)
    private LocalDateTime rechargeTime;

    /**
     * 产生这笔充值记录的订单号
     */
    @Column
    private String orderNo;

    /**
     * 员工
     */
    @ManyToOne
    @JoinColumn(name = "emp_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private OrgEmployee employee;

    @JoinColumn(name = "org_id")
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Org org;

    /**
     * 类型 0-自己充值的, 1-后台处理的
     */
    @Column(nullable = false)
    private Integer typ;

    /**
     * 是否已充值后台
     */
    private Boolean recharge = false;

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
    private Long rechargeRecordId;

    /**
     * 同步到新开普状态
     */
    private Boolean syncToEscp;

    /**
     * 同步到新开普次数
     */
    private Integer syncToEscpTimes;
}
