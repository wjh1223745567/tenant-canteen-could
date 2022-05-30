package com.iotinall.canteen.service.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iotinall.canteen.constants.WXConstants;
import com.iotinall.canteen.dto.message.WxMiniProgramSubscribeMessage;
import com.iotinall.canteen.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.Jscode2sessionResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信基类
 *
 * @author loki
 * @date 2020/09/27 17:37
 */
@Slf4j
@Service
public class BaseWxService {
    @Value("${wx.mini.appId}")
    protected String appId;
    @Value("${wx.mini.appSecret}")
    protected String appSecret;
    @Value("${wx.mini.miniprogramState}")
    protected String miniprogramState;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取小程序appId获取appSecret
     *
     * @author loki
     * @date 2020/09/28 16:22
     */
    public Map<String, String> getAppIdAndAppSecret() {
        Map<String, String> result = new HashMap<>();
        result.put("appId", appId);
        result.put("appSecret", appSecret);
        return result;
    }

    /**
     * 获取用户openId
     *
     * @author loki
     * @date 2020/09/27 18:12
     */
    public Jscode2sessionResult getUserOpenId(String code) {
        Jscode2sessionResult snsToken = SnsAPI.jscode2session(appId, appSecret, code);
        return snsToken;
    }

    /**
     * 刷新TOKEN
     *
     * @author loki
     * @date 2020/09/25 16:10
     */
    public String getToken() {
        final String ACCESS_TOKEN_KEY = "wx:access_token";
        String access_token = redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);
        log.info("获取缓存wxtoken：{}", access_token);
        return access_token;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecretValue() {
        return appSecret;
    }

    /**
     * 发送消息
     *
     * @author loki
     * @date 2020/09/25 16:18
     */
    protected void sendMessage(WxMiniProgramSubscribeMessage message) {
        message.setMiniprogramState(miniprogramState);
        try {
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Content-Type", "application/json");
            log.info("发送微信订阅消息：{}", JSON.toJSONString(message));
            Object result = RestTemplateUtil.post(WXConstants.SEND_MINI_PROGRAM_SUB_MESSAGE_URL + getToken(), headers, message, Object.class);
            log.info("发送微信订阅消息结果：{}", JSONObject.toJSONString(result));
        } catch (Exception ex) {
            log.info("发送消息失败");
        }
    }
}
