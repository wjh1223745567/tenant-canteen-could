package com.iotinall.canteen.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "default-mysql-config")
public class DefaultMysqlConfigProperty {

    private Integer minimumIdle;

    private Integer maximumPoolSize;

    private Boolean autoCommit;

    private Long idleTimeout;

    private Long connectionTimeout;

    private Long maxLifetime;

    private String connectionTestQuery;

    /**
     * url后缀
     */
    public static final String urlSuffix = "?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&useSSL=false&allowMultiQueries=true&AllowPublicKeyRetrieval=True&autoReconnect=true&failOverReadOnly=false";

    /**
     * url前缀
     */
    public static final String urlPrefix = "jdbc:mysql://";

}
