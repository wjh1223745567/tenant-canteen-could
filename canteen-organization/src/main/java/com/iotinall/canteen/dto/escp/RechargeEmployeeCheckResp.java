package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 充值校验返回参数
 * @author loki
 * @date 2021/6/24 14:40
 **/
@Data
public class RechargeEmployeeCheckResp implements Serializable {
    @JsonProperty(value = "card_no")
    private String cardNo;

    @JsonProperty(value = "school_id")
    private String schoolId;

    @JsonProperty(value = "student_id")
    private String studentId;

    @JsonProperty(value = "return_code")
    private String returnCode;

    @JsonProperty(value = "return_message")
    private String returnMessage;
}
