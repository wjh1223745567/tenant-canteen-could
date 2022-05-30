package com.iotinall.canteen.netty.manager;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.netty.manager.base.BaseHttpKeepAliveManager;
import com.iotinall.canteen.netty.manager.factory.ChannelFactory;
import com.iotinall.canteen.netty.manager.factory.HttpResponseFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class HttpKeepAliveManager extends BaseHttpKeepAliveManager {

    private static final Logger KEEP_ALIVE_LOGGER = LoggerFactory.getLogger(HttpKeepAliveManager.class);
    private static final int KEEP_ALIVE_CONNECTION_TIME_OUT = 10000;
    private static final int KEEP_LOG_INFO_REQUEST_JSON = 200;

    /**
     * 通过长连接获取返回结果
     *
     * @param serialNo    序列号
     * @param httpRequest 请求类
     * @return
     * @throws InterruptedException
     */
    public synchronized static Map<String, String> getResponseMapByKeepAliveConnection(String serialNo, FullHttpRequest httpRequest) throws InterruptedException {
        Map<String, String> responseMap = null;
        Integer responseKey = null;
        try {
            //通过设备编码获取，通道
            ChannelHandlerContext ctx = ChannelFactory.getChannelByClientId(serialNo);

            if (Objects.isNull(ctx)) {
                return null;
            }
            //发起请求前，先做移除
            responseKey = ctx.hashCode();
            HttpResponseFactory.RESPONSE_DATA.remove(responseKey);
            String response = "";
            if (ctx.channel().isWritable()) {
                ctx.writeAndFlush(httpRequest);
                //处理成 responseMap
                response = HttpResponseFactory.getResponse(ctx, KEEP_ALIVE_CONNECTION_TIME_OUT);
            } else {
                KEEP_ALIVE_LOGGER.error("[DEVICE_CODE = {}]...... netty queue is line up, return fail ......", serialNo);
                ctx.channel().close();
            }

            KEEP_ALIVE_LOGGER.info("[{}][key = {}][响应结果]: {}", serialNo, responseKey, response);
            if (StringUtils.isNotBlank(response)) {
                response.replace("\"Data\": \"null\"", "\"Data\":{}");
                responseMap = JSON.parseObject(response, Map.class);
            }
        } catch (Exception e) {
            KEEP_ALIVE_LOGGER.error(e.getMessage(), e);
            //异常移除链接
            if (!ChannelFactory.removeChannelByClientId(serialNo)) {
                KEEP_ALIVE_LOGGER.error("[REMOVE_CHANNEL], deviceCode={}, remove error", serialNo);
            }
        } finally {
            if (Objects.nonNull(responseKey)) {
                //移除数据 无论是否取到，做移除
                HttpResponseFactory.RESPONSE_DATA.remove(responseKey);
            }
        }
        return responseMap;
    }
}
