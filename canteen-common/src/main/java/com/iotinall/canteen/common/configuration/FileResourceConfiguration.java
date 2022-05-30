package com.iotinall.canteen.common.configuration;

import org.csource.common.FastdfsConfig;
import org.csource.fastdfs.client.FastdfsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author WJH
 * @date 2019/12/2718:09
 */
@Configuration
public class FileResourceConfiguration {

    @Bean
    public FastdfsConfig fastdfsConfig() {
        return new FastdfsConfig();
    }

    @Bean
    public FastdfsClient fastdfsClient(FastdfsConfig fastdfsConfig) {
        return new FastdfsClient(fastdfsConfig);
    }
}
