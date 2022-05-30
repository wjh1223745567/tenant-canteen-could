package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.iotinall.canteen.dto.hikvision.HicEventReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 海康威视设备对接
 *
 * @author loki
 * @date 2021/7/2 11:42
 **/
@Slf4j
@Service
@RefreshScope
public class HikVisionDeviceService {

    private static final String ARTEMIS_PATH = "/artemis";

    private static final String contentType = "application/json";

    /**
     * 订阅事件，重复订阅不会重复触发。
     */
    private static final String EVENT_SERVICE = "/api/eventService/v1/eventSubscriptionByEventTypes";

    @Value("${hikvision.httpType}")
    private String httpType;

    @Value("${hikvision.host}")
    private String host;

    @Value("${hikvision.appKey}")
    private String appKey;

    @Value("${hikvision.appSecret}")
    private String appSecret;

    @Value("${hikvision.eventCallBack}")
    private String eventCallBack;

    /**
     * 默认事件
     */
    @Value("${hikvision.defaultEvents}")
    private String defaultEvents;

    @PostConstruct
    public void init() {
        ArtemisConfig.host = host; // artemis网关服务器ip端口
        ArtemisConfig.appKey = appKey;  // 秘钥appkey
        ArtemisConfig.appSecret = appSecret;// 秘钥appSecret
    }

    /**
     * 获取请求路径
     *
     * @param url
     * @return
     */
    private Map<String, String> getUrl(String url) {
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(httpType + "://", ARTEMIS_PATH + url);//根据现场环境部署确认是http还是https
            }
        };
        return path;
    }

    /**
     * 订阅事件
     */
    public void subscribeToEvents() {
        HicEventReq hicEventReq = new HicEventReq()
                .setEventTypes(Arrays.stream(defaultEvents.split(",")).map(Integer::valueOf).collect(Collectors.toList()))
                .setEventDest(eventCallBack);
        String result = ArtemisHttpUtil.doPostStringArtemis(getUrl(EVENT_SERVICE), JSON.toJSONString(hicEventReq), null, null, contentType, null);// post请求application/json类型参数
        log.info("订阅事件接口返回：{}", result);
    }
}
