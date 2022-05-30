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
public class RechargeEmployeeApplyResp implements Serializable {
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
     * 企业编码
     */
    @JsonProperty(value = "school_id")
    private String school_id;

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
    @JsonProperty(value = "card_balance")
    private Long cardBalance;

    /**
     * 未结算余额，单位为元。不 含小 数
     */
    @JsonProperty(value = "unsettle_amount")
    private Long unsettleAmount;

    /**
     * 库余额，单位为元。不含小数
     */
    @JsonProperty(value = "db_balance")
    private Long dbBalance;

    @JsonProperty(value = "return_code")
    private String returnCode;

    @JsonProperty(value = "return_message")
    private String returnMessage;
}
