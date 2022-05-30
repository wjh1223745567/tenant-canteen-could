package com.iotinall.canteen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.Jscode2sessionResult;

import javax.annotation.Resource;
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
@RefreshScope
public class BaseWxService {
    @Value("${wx.mini.appId:null}")
    protected String appId;
    @Value("${wx.mini.appSecret:null}")
    protected String appSecret;

    @Resource
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
}
