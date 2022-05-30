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
public class RechargeEmployeeApplyReq implements Serializable {
    @JsonProperty(value = "card_no")
    private String cardNo;

    @JsonProperty(value = "student_id")
    private String studentId;

    @JsonProperty(value = "student_name")
    private String studentName;

    @JsonProperty(value = "apply_id")
    private String applyId;

    @JsonProperty(value = "apply_amount")
    private Long applyAmount;

    /**
     * 可能的支付充值类型： 10：自动充值 20：多媒体充值 30： PC 充值 40：手机客户端充值 50：短信充值
     */
    @JsonProperty(value = "type")
    private Integer type;
}
