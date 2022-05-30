package com.iotinall.canteen.utils;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loki
 * @date 2020/06/09 14:20
 */
public class SingletonRestTemplate {

    static final RestTemplate INSTANCE = registerTemplate();
    static final RestTemplate STREAM_INSTANCE = registersStreamTemplate();


    public static RestTemplate getInstance() {
        return SingletonRestTemplate.INSTANCE;
    }

    public static RestTemplate getStreamInstance() {
        return SingletonRestTemplate.STREAM_INSTANCE;
    }

    public static RestTemplate registersStreamTemplate() {
        StreamRestTemplate restTemplate = new StreamRestTemplate();
        restTemplate.setMessageConverters(getConverts());
        return restTemplate;
    }

    public static RestTemplate registerTemplate() {
        RestTemplate restTemplate = new RestTemplate(getFactory());
        restTemplate.setMessageConverters(getConverts());
        return restTemplate;
    }


    private static SimpleClientHttpRequestFactory getFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);
        return factory;
    }

    private static List<HttpMessageConverter<?>> getConverts() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
                SerializerFeature.QuoteFieldNames, SerializerFeature.DisableCircularReferenceDetect);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        messageConverters.add(fastConverter);

        StringHttpMessageConverter stringConvert = new StringHttpMessageConverter();
        List<MediaType> stringMediaTypes = new ArrayList<MediaType>() {{
            add(MediaType.TEXT_PLAIN);
            add(MediaType.TEXT_HTML);
            add(MediaType.IMAGE_JPEG);
            add(MediaType.APPLICATION_JSON_UTF8);
        }};

        stringConvert.setSupportedMediaTypes(stringMediaTypes);
        messageConverters.add(stringConvert);
        return messageConverters;
    }
}
