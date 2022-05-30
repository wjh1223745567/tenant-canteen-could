package com.iotinall.canteen.netty.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iotinall.canteen.netty.manager.factory.ChannelFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * tcp消息帮助类
 *
 * @author loki
 * @date 2021/02/23 13:54
 */
@Slf4j
public class TcpHelper {

    /**
     * 发送消息
     *
     * @author loki
     * @date 2021/02/23 13:56
     */
    public static void sendByClientId(String clientId, Object msg) {
        ChannelHandlerContext channel = ChannelFactory.getChannelByClientId(clientId);
        if (null == channel) {
            log.info("客户端未连接:{}", clientId);
            return;
        }

        sendMsg(channel, msg);
    }

    /**
     * 发送消息
     *
     * @author loki
     * @date 2021/02/23 13:56
     */
    public static void sendByType(String type, Object msg) {
        List<ChannelHandlerContext> channelList = ChannelFactory.getChannelContextByType(type);
        if (CollectionUtils.isEmpty(channelList)) {
            log.info("客户端未连接");
            return;
        }

        for (ChannelHandlerContext channel : channelList) {
            sendMsg(channel, msg);
        }
    }

    private static void sendMsg(ChannelHandlerContext channel, Object msg) {
//        try {
//            ByteBuf byteBuf = Unpooled.buffer();
//            byteBuf.writeBytes(msg);
//            channel.writeAndFlush(byteBuf);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
