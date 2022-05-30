package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constant.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 账单明细
 *
 * @author loki
 * @date 2021/01/14 15:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "wallet_bill_detail")
public class WalletBillDetail extends BaseEntity {
    /**
     * 金额变动值
     */
    private BigDecimal amount;

    /**
     * 0-减少 1-添加
     */
    private Integer type;

    /**
     * 变动原因
     */
    private Integer reason;


    /**
     * 记录类型：0-充值 1-消费 2-取消充值 3-消费退款
     */
    private Integer recordType;

    /**
     * 钱包ID
     */
    private Long walletId;

    /**
     * 关联操作记录ID：消费记录ID或者充值记录ID
     */
    private Long recordId;

    /**
     * 账单来源
     */
    private Integer source;

    /**
     * 智慧餐厅消费记录类型
     *
     * @author loki
     * @date 2021/01/14 15:46
     */
    public Integer getBillDetailReason(Integer mealType) {
        //1-早餐 2-午餐 3-晚餐
        if (mealType == 1) {
            return Constants.WALLET_BILL_DETAIL_REASON_CONSUME_BREAKFAST;
        } else if (mealType == 2) {
            return Constants.WALLET_BILL_DETAIL_REASON_CONSUME_LUNCH;
        } else if (mealType == 4) {
            return Constants.WALLET_BILL_DETAIL_REASON_CONSUME_DINNER;
        } else if (mealType == 8) {
            return Constants.WALLET_BILL_DETAIL_TAKE_OUT;
        }
        return null;
    }

    /**
     * 闸机通行系统同步过来的消费记录类型
     *
     * @author loki
     * @date 2021/01/14 15:46
     */
    public Integer getBillDetailReason2(Integer mealType) {
        //1-早餐 2-午餐 3-晚餐
        if (mealType == 1) {
            return Constants.WALLET_BILL_DETAIL_REASON_CONSUME_BREAKFAST;
        } else if (mealType == 2) {
            return Constants.WALLET_BILL_DETAIL_REASON_CONSUME_LUNCH;
        } else if (mealType == 3) {
            return Constants.WALLET_BILL_DETAIL_REASON_CONSUME_DINNER;
        }
        return null;
    }
}
