package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取余额列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class BalanceListDTO implements Serializable {
    /**
     * 姓名
     */
    @JsonProperty(value = "username")
    private String username;
    /**
     * 用户证件号码
     */
    @JsonProperty(value = "idserial2")
    private String idSerial2;

    /**
     * 工号
     */
    @JsonProperty(value = "idserial")
    private String idSerial;

    /**
     * 总余额，单位分（余额=卡余额+主钱包未领款+补助钱包余额+补助钱包未领款+其他余额1+其他余额2
     */
    @JsonProperty(value = "totaloddfare")
    private Long totalOddFare;

    /**
     * 主钱包余额（分）
     */
    @JsonProperty(value = "cardoddfare")
    private Long cardOddFare;

    /**
     * 主钱包未领款（分）
     */
    @JsonProperty(value = "uncardfare")
    private Long unCardFare;

    /**
     * 补助钱包余额（分）
     */
    @JsonProperty(value = "subcardoddfare")
    private Long subCardOddFare;

    /**
     * 补助钱包未领款（分）
     */
    @JsonProperty(value = "subuncardfare")
    private Long subUnCardFare;

    /**
     * 其他余额1（分）
     */
    @JsonProperty(value = "otherfare1")
    private Long otherFare1;

    /**
     * 其他余额2（分）
     */
    @JsonProperty(value = "otherfare2")
    private Long otherFare2;
}
