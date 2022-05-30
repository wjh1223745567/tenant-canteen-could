package com.iotinall.canteen.netty.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.netty.manager.FullHttpRequestManager;
import com.iotinall.canteen.netty.manager.HttpKeepAliveManager;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * socket消息发送工具类
 *
 * @author loki
 * @date 2021/01/09 11:53
 */
@Slf4j
public class HttpRequestHelper {

    public static Map<String, String> get(String api, String serialNo, Object data) {
        return request(api, serialNo, data, HttpMethod.GET);
    }

    public static Map<String, String> post(String api, String serialNo, Object data) {
        return request(api, serialNo, data, HttpMethod.POST);
    }

    public static Map<String, String> put(String api, String serialNo, Object data) {
        return request(api, serialNo, data, HttpMethod.PUT);
    }

    public static Map<String, String> delete(String api, String serialNo, Object data) {
        return request(api, serialNo, data, HttpMethod.DELETE);
    }

    /**
     * 同步人员到终端
     */
    public static Map<String, String> request(String api, String serialNo, Object data, HttpMethod method) {
        String json = JSON.toJSONString(data);

        log.info("请求路径：{}", api);
        log.info("请求参数：{}", json);
        try {
            FullHttpRequest httpRequest = createHttpRequest(api, method, json);

            return HttpKeepAliveManager.getResponseMapByKeepAliveConnection(serialNo, httpRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("请求失败");
        }
        return null;
    }

    /**
     * 创建请求
     *
     * @param api    请求路径
     * @param method 请求方式
     * @param data   请求数据
     * @return 请求
     * @throws URISyntaxException
     */
    private static FullHttpRequest createHttpRequest(String api, HttpMethod method, String data) throws URISyntaxException {
        if (method.equals(HttpMethod.POST)) {
            return FullHttpRequestManager.createPostKeepAliceRequest(api, data);
        } else if (method.equals(HttpMethod.DELETE)) {
            return FullHttpRequestManager.createDeleteKeepAliceRequest(api, data);
        } else if (method.equals(HttpMethod.PUT)) {
            return FullHttpRequestManager.createPutKeepAliceRequest(api, data);
        } else if (method.equals(HttpMethod.GET)) {
            return FullHttpRequestManager.createGetKeepAliceRequest(api, data);
        }
        return null;
    }
}
