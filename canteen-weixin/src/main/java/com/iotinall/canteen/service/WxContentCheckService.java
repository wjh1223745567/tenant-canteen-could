package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iotinall.canteen.service.base.BaseWxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Service;
import weixin.popular.client.LocalHttpClient;

import java.nio.charset.StandardCharsets;

/**
 * 微信敏感词校验
 *
 * @author loki
 * @date 2020/09/27 17:48
 */
@Slf4j
@Service
public class WxContentCheckService extends BaseWxService {
    private static final String WX_CHECK_URL = "https://api.weixin.qq.com/wxa/msg_sec_check";

    /**
     * 文本敏感词校验
     *
     * @author loki
     * @date 2020/10/29 17:46
     */
    public Boolean check(String content) {
        try {
            String json = String.format("{\"content\":\"%s\"}", content);
            BasicHeader jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            HttpUriRequest httpUriRequest = RequestBuilder.post()
                    .setHeader(jsonHeader)
                    .setUri(WX_CHECK_URL)
                    .addParameter("access_token", getToken())
                    .setEntity(new StringEntity(json, StandardCharsets.UTF_8))
                    .build();
            String result = LocalHttpClient.executeJsonResult(httpUriRequest, String.class);
            return getResult(JSON.parseObject(result));
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    /**
     * 判断是否违规
     *
     * @author loki
     * @date 2020/10/29 17:46
     */
    private Boolean getResult(JSONObject jso) {
        Object errcode = jso.get("errcode");
        int errCode = (int) errcode;
        if (errCode == 0) {
            return true;
        } else if (errCode == 87014) {
            log.info("内容违规-----------");
            return false;
        } else {
            log.info("验证失败：{}", JSON.toJSONString(jso));
        }
        return true;
    }
}
