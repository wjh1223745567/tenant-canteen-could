package com.iotinall.canteen.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sso")
public class SsoProperty {

    /**
     * 认证服务器连接
     */
    private String ssoUrl;

    private String appId;

    private String appSecurity;

    private String username;

    private String password;

    /**
     * 回调地址
     */
    private String backUrl;

}
