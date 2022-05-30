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
public class TransactionListDTO implements Serializable {
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
     * 交易时间yyyy-MM-dd hh:mm:ss
     */
    @JsonProperty(value = "txdate")
    private String txDate;

    /**
     * 交易钱包类型0主钱包 1其他
     */
    @JsonProperty(value = "subareano")
    private String subAreaNo;

    /**
     * 操作类型0充值 1消费
     */
    @JsonProperty(value = "flag")
    private String flag;

    /**
     * 交易金额（分）
     */
    @JsonProperty(value = "txamt")
    private String txAmt;

    /**
     * 事由描述
     */
    @JsonProperty(value = "description")
    private String description;

    /**
     * 终端信息
     */
    @JsonProperty(value = "termname")
    private String termName;

    /**
     * 余额（分）
     */
    @JsonProperty(value = "cardoddfare")
    private String cardOddFare;
}
