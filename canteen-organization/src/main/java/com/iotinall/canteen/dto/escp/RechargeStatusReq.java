package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取充值结果
 * @author loki
 * @date 2021/6/24 14:40
 **/
@Data
public class RechargeStatusReq implements Serializable {
    @JsonProperty(value = "")
    private String cardNo;

    @JsonProperty(value = "student_id")
    private String studentId;

    @JsonProperty(value = "out_id")
    private String outId;
}
