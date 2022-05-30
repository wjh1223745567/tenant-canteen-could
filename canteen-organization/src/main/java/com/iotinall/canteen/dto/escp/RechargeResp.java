package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 充值请求参数
 *
 * @author loki
 * @date 2021/6/23 17:39
 **/
@Accessors(chain = true)
@Data
public class RechargeResp implements Serializable {
    /**
     * 开发者申请的应用 ID
     */
    @JsonProperty(value = "client_id")
    private String clientId;

    /**
     * 接入应用编号；固定值,接入时会分配
     */
    @JsonProperty(value = "app_id")
    private Integer appId;

    /**
     * 支付方式编码；固定值,接入时会分配
     */
    @JsonProperty(value = "pay_code")
    private String payCode;

    /**
     * 企业编码 (节点中的 school_id 必须一致)
     */
    @JsonProperty(value = "school_id")
    private String schoolId;

    /**
     * 随机数，最大 32 位，保证签名不可 预测,建议：时间戳+16 位随机数
     */
    @JsonProperty(value = "norce_str")
    private String norceStr;

    /**
     * JSON 数组节点，一下几个参数为此节点的 子节点； (最多 50 个子节点，建议不要超 过 25)
     */
    @JsonProperty(value = "data")
    private Object data;
}
