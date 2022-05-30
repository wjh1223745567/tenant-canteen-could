package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 虚拟卡消费接口返回
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class ConsumeResp implements Serializable {
    /**
     * 终端编号(需提前在平台创建并绑定商户)
     */
    @JsonProperty(value = "poscode")
    private String posCode;

    /**
     * 返回值 错误码 200代表成功
     */
    @JsonProperty(value = "result")
    private String result;

    /**
     * 返回的提示消息
     */
    @JsonProperty(value = "message")
    private String message;
    /**
     * 交易订单号
     */
    @JsonProperty(value = "orderno")
    private String orderNo;
    /**
     * 工号
     */
    @JsonProperty(value = "idserial")
    private String idSerial;
    /**
     * 交易金额单位分
     */
    @JsonProperty(value = "opfare")
    private String opFare;
    /**
     * 折扣/管理费(单位分 当折扣时为负)
     */
    @JsonProperty(value = "farerate")
    private String fareRate;
    /**
     * 实际交易时间例：yyyy-MM-dd HH:mm:ss
     */
    @JsonProperty(value = "txdt")
    private String txDt;
    /**
     * 主钱包余额单位分
     */
    @JsonProperty(value = "oddfare")
    private Long oddFare;
    /**
     * 补助钱包余额单位分
     */
    @JsonProperty(value = "subfare")
    private Long subFare;

    /**
     * 账户余额(主钱包余额 + 补助钱包余额)单位分
     */
    @JsonProperty(value = "totelfare")
    private Long toTelFare;
}
