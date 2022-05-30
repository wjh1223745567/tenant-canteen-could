package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 虚拟卡消费查询请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
@Accessors(chain = true)
public class ConsumeQueryReq implements Serializable {
    /**
     * 终端编号(需提前在平台创建并绑定商户)
     */
    @JsonProperty(value = "poscode")
    private String posCode;

    /**
     * 终端支付订单编号
     */
    @JsonProperty(value = "termtradeno")
    private String termTradeNo;
}
