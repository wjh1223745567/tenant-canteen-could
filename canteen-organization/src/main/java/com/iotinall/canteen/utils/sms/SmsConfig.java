package com.iotinall.canteen.utils.sms;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 短信配置
 *
 * @author loki
 * @date 2020/04/29 11:27
 */
@Data
@Accessors(chain = true)
public class SmsConfig {
    /**
     * AppID
     */
    private Integer appId;
    /**
     * AppKey
     */
    private String appKey;
    /**
     * 短信模板ID
     */
    private Integer templateId;
    /**
     * 签名内容
     */
    private String sign;
}
