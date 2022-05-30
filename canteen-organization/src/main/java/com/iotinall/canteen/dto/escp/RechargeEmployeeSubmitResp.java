package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 一卡通充值申请接口
 *
 * @author loki
 * @date 2021/6/24 14:40
 **/
@Data
public class RechargeEmployeeSubmitResp implements Serializable {
    /**
     * 对应请求验证参数中的一卡通卡号码
     */
    @JsonProperty(value = "card_no")
    private String cardNo;

    /**
     * 员工工号
     */
    @JsonProperty(value = "student_id")
    private String studentId;

    /**
     * 充值申请流水号
     */
    @JsonProperty(value = "apply_id")
    private String applyId;

    /**
     * 一卡通业务流水号
     */
    @JsonProperty(value = "out_id")
    private String outId;

    /**
     * 卡余额，正整数，单位：元
     */
    @JsonProperty(value = "recharge_amount")
    private Long rechargeAmount;

    /**
     * 格式为：yyyyMMddhhmmss 样例: 20130318102012
     */
    @JsonProperty(value = "pay_time")
    private String payTime;

    /**
     * 三方支付平台的每一个一卡通充值 请求对应一个唯一的单据号。 三方支付平台如果有则传，
     * 如果没 有则传申请充值流水号(apply_id 字 段, 和 api/1/ecard/recharge/apply 中 的此字段名称和值相同)
     */
    @JsonProperty(value = "recharge_order_no")
    private String rechargeOrderNo;
}
