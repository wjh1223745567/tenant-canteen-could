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
public class RechargeEmployeeSubmitReq implements Serializable {
    @JsonProperty(value = "recharge_order_no")
    private String rechargeOrderNo;

    @JsonProperty(value = "student_id")
    private String studentId;

    @JsonProperty(value = "card_no")
    private String cardNo;

    @JsonProperty(value = "out_id")
    private String outId;

    @JsonProperty(value = "recharge_amount")
    private Long rechargeAmount;

    @JsonProperty(value = "pay_time")
    private String payTime;

    @JsonProperty(value = "apply_id")
    private String applyId;
}
