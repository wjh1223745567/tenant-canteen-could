package com.iotinall.canteen.common.configuration;

import com.iotinall.canteen.common.security.AuthorizationTokenFilter;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign配置
 * 使用FeignClient进行服务间调用，传递headers信息
 */
@Slf4j
@Configuration
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if(attributes != null){
            HttpServletRequest request = attributes.getRequest();
            //添加token
            requestTemplate.header(AuthorizationTokenFilter.TOKEN_KEY, request.getHeader(AuthorizationTokenFilter.TOKEN_KEY));
            requestTemplate.header(AuthorizationTokenFilter.APP_TOKEN_KEY, request.getHeader(AuthorizationTokenFilter.APP_TOKEN_KEY));
        }
    }
}