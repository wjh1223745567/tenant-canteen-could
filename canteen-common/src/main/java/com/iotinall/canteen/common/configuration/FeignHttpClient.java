package com.iotinall.canteen.common.configuration;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class FeignHttpClient {

    @Bean
    public CloseableHttpClient httpClient(HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(200);
        clientConnectionManager.setDefaultMaxPerRoute(30);
        return clientConnectionManager;
    }

    @Bean
    public HttpClientBuilder httpClientBuilder(PoolingHttpClientConnectionManager clientConnectionManager) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(20000)
                .setSocketTimeout(20000)
                .setConnectionRequestTimeout(20000)
                .build();
        HttpClientBuilder builder = HttpClientBuilder.create();
        Header[] defaultHeaders = {new BasicHeader("charset", "utf-8"),
                new BasicHeader("content-type", "application/json")};
        builder.setDefaultHeaders(Arrays.asList(defaultHeaders));
        builder.setConnectionManager(clientConnectionManager);
        builder.setDefaultRequestConfig(requestConfig);
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        builder.addInterceptorFirst(new FeignResponseInterceptor());
        return builder;
    }

}
