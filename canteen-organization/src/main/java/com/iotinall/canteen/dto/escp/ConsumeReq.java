package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 虚拟卡消费请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
@Accessors(chain = true)
public class ConsumeReq implements Serializable {
    /**
     * 终端编号(需提前在平台创建并绑定商户)
     */
    @JsonProperty(value = "poscode")
    private String posCode;

    /**
     * 交易金额单位分
     */
    @JsonProperty(value = "opfare")
    private Long opFare;

    /**
     * 交易时间例：yyyyMMddHHmmss
     */
    @JsonProperty(value = "opdt")
    private String opDt;

    /**
     * 交易类型
     */
    @JsonProperty(value = "tradetype")
    private String tradeType;

    /**
     * 钱包类型
     */
    @JsonProperty(value = "wallettype")
    private Integer walletType = 0;

    /**
     * 三方订单号
     */
    @JsonProperty(value = "thirdno")
    private String thirdNo;

    /**
     * 工号
     */
    @JsonProperty(value = "idserial")
    private String idSerial;

    /**
     * 姓名
     */
    @JsonProperty(value = "username")
    private String username;

    /**
     * 是否是脱机数据(0在线，1脱机)
     */
    @JsonProperty(value = "isonline")
    private Integer isOnline = 1;
}
