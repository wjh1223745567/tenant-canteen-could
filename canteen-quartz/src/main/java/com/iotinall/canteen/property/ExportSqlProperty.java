package com.iotinall.canteen.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "exportsql")
@RefreshScope
public class ExportSqlProperty {

    /**
     * 包括的数据库，逗号分隔，为空备份所有
     */
    private String included;

    /**
     * 不包括的数据库
     */
    private String excluded;

    /**
     * 插入语句批量数量
     */
    private Integer insertSize;

    /**
     * zip解密密码
     */
    private String zippassword;

    /**
     * 保留多少天
     */
    private Integer keepDays;

}
