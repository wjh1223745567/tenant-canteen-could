package com.iotinall.canteen.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 华为设备对接参数
 *
 * @author loki
 * @date 2021/6/28 19:42
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "huawei")
public class HuaweiProperty {
    /**
     * 服务ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 登入名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 区域编码
     */
    private String domainCode;
}
