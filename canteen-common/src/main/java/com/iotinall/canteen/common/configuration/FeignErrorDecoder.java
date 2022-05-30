package com.iotinall.canteen.common.configuration;

import com.iotinall.canteen.common.exception.BizException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String msg = "服务器接口出错";
        if(Objects.equals(response.status(), 401) || Objects.equals(response.status(), 403)){
            msg = "接口未授权或未登录";
        }
        log.error("feign调用接口出错：{},{}", response.status(), response.request().url());
        return new BizException(String.valueOf(response.status()), msg);
    }
}
