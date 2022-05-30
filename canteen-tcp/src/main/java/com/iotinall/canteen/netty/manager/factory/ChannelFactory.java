package com.iotinall.canteen.netty.manager.factory;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道管理工厂
 *
 * @author loki
 * @date 2020/06/02 16:14
 */
@Slf4j
public class ChannelFactory {
    protected static final Map<String, ChannelContext> CHANNEL_MAP = new ConcurrentHashMap<>(256);

    /**
     * 删除通道
     *
     * @param clientId 设备编码
     */
    public static boolean removeChannelByClientId(String clientId) throws InterruptedException {
        ChannelContext context = CHANNEL_MAP.get(clientId);
        if (Objects.nonNull(context)) {
            CHANNEL_MAP.remove(clientId);
            log.info("移除掉线客户端：{}", context.getClientId());
            context.getChannel().close();
            return true;
        } else {
            //CPU
            Thread.sleep(1);
        }
        return false;
    }

    /**
     * 新增在线通道
     *
     * @author loki
     * @date 2020/06/08 10:49
     */
    public static void addChannel(String type, String clientId, ChannelHandlerContext ctx) {
        ChannelContext context;
        if (CHANNEL_MAP.containsKey(clientId)) {
            context = CHANNEL_MAP.get(clientId);
            // context.getChannel().close();
        } else {
            context = new ChannelContext();
            CHANNEL_MAP.put(clientId, context);
        }
        context.setChannel(ctx);
        context.setClientId(clientId);
        context.setType(type);
        context.setChannelId(getChannelId(ctx.channel()));
    }

    /**
     * 删除通道
     */
    public static boolean removeChannelByChannelId(Channel channel) {
        return removeChannelByChannelId(getChannelId(channel));
    }

    /**
     * 删除通道
     */
    public static boolean removeChannelByChannelId(String channelId) {
        for (ChannelContext context : CHANNEL_MAP.values()) {
            if (!context.getChannelId().equals(channelId)) {
                continue;
            }
            context.getChannel().close();
            CHANNEL_MAP.remove(context.getClientId());
            log.info("移除掉线客户端：{}", context.getClientId());
            return true;
        }
        return false;
    }

    /**
     * 根据设备序列号获取通道
     *
     * @author loki
     * @date 2020/06/08 10:51
     */
    public static ChannelHandlerContext getChannelByClientId(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            log.error("获取通道失败，设备序列号 = {} ", clientId);
            return null;
        }
        ChannelContext context = CHANNEL_MAP.get(clientId);
        return context == null ? null : context.getChannel();
    }

    /**
     * 获取通道IP
     *
     * @author loki
     * @date 2020/06/06 17:41
     */
    public static String getChannelIp(ChannelHandlerContext ctx) {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }

    /**
     * 根据channel 类型获取channel
     *
     * @author loki
     * @date 2021/02/23 14:10
     */
    public static List<ChannelHandlerContext> getChannelContextByType(String type) {
        if (CollectionUtils.isEmpty(CHANNEL_MAP)) {
            return null;
        }

        List<ChannelHandlerContext> contextList = new ArrayList<>();
        for (Map.Entry<String, ChannelContext> channel : CHANNEL_MAP.entrySet()) {
            if (channel.getValue().getType().equals(type)) {
                contextList.add(channel.getValue().getChannel());
            }
        }

        return contextList;
    }

    /**
     * 获取渠道ID
     *
     * @author loki
     * @date 2021/7/8 10:31
     **/
    private static String getChannelId(Channel channel) {
        return channel.id().asLongText();
    }

    /**
     * 根据渠道ID获取客户端
     *
     * @author loki
     * @date 2021/7/8 10:30
     **/
    public static String getClientIdByChannelId(ChannelHandlerContext ctx) {
        String channelId = getChannelId(ctx.channel());

        for (ChannelContext context : CHANNEL_MAP.values()) {
            if (!context.getChannelId().equals(channelId)) {
                continue;
            }

            return context.getClientId();
        }

        return null;
    }
}
