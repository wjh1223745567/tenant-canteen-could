package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author loki
 * @date 2021/6/24 14:40
 **/
@Data
public class RechargeEmployeeCheckReq implements Serializable {
    @JsonProperty(value = "card_no")
    private String cardNo;

    @JsonProperty(value = "student_id")
    private String studentId;

    @JsonProperty(value = "student_name")
    private String studentName;

    @JsonProperty(value = "accept_time")
    private String acceptTime;
}
