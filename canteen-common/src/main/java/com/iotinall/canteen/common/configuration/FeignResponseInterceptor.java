package com.iotinall.canteen.common.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class FeignResponseInterceptor implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse response, HttpContext context) throws IOException {
        response.setEntity(new BufferedHttpEntity(response.getEntity()));
        String result = EntityUtils.toString(response.getEntity(), "UTF-8");
        if(result.startsWith("{") && StringUtils.isNotBlank(result)){
            Map<String, Object> map = JSON.parseObject(result, new TypeReference<Map<String, Object>>(){});
            if(validParam(map)){
                ResultDTO<Object> resultDTO = JSON.parseObject(result, new TypeReference<ResultDTO<Object>>(){});
                if(!(resultDTO.getCode() == null && resultDTO.getData() == null && resultDTO.getMsg() == null) && !Objects.equals(resultDTO.getCode(), "0")){
                    throw new BizException(resultDTO.getCode(), resultDTO.getMsg());
                }
            }
        }
    }

    /**
     * 验证参数
     * @return
     */
    private Boolean validParam(Map<String, Object> resultDTO){
        Set<String> keys = resultDTO.keySet();
        if(keys.size() == 3 && resultDTO.containsKey("code") && resultDTO.containsKey("data") && resultDTO.containsKey("msg")){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}
