package com.iotinall.canteen.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 新开普接口调用参数
 *
 * @author loki
 * @date 2021/6/23 14:24
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "escp")
public class EscpProperty {

    /**
     * 签名
     */
    @JsonProperty(value = "sign-key")
    private String signKey;

    /**
     * 充值 接入应用编号；固定值,接入时会分配
     */
    @JsonProperty(value = "app-id")
    private Integer appid;

    /**
     * 充值 开发者申请的应用 ID
     */
    @JsonProperty(value = "client-id")
    private String clientId;

    /**
     * 充值 支付方式编码；固定值,接入时会分配
     */
    @JsonProperty(value = "pay-code")
    private String payCode;

    /**
     * 消费 终端编号(需提前在平台创建并绑定商户)
     */
    @JsonProperty(value = "pos-code")
    private String posCode;

    /**
     * 企业编码
     */
    @JsonProperty(value = "school_id")
    private String schoolId;

    /**
     * 接口
     */
    @JsonProperty(value = "api")
    private Map<String, String> apiList;


    /**
     * 获取api
     **/
    public String getApiUrl(String type) {
        return this.apiList.get(type);
    }
}
