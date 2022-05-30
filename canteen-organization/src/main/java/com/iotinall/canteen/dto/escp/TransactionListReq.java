package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取注销人员列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class TransactionListReq implements Serializable {
    /**
     * 工号
     */
    @JsonProperty(value = "idserial")
    private String idSerial;

    /**
     * 开始时间，格式：yyyy-MM-dd hh:mm:ss
     */
    @JsonProperty(value = "startdate")
    private String startDate;

    /**
     * 结束时间，格式：yyyy-MM-dd hh:mm:ss
     */
    @JsonProperty(value = "enddate")
    private String endDate;

    /**
     * 操作类型0充值 1消费 空或-1全部
     */
    @JsonProperty(value = "flag")
    private Integer flag;
}
