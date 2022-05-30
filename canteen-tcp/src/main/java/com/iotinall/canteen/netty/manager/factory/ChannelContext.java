package com.iotinall.canteen.netty.manager.factory;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * 管道上下文
 *
 * @author loki
 * @date 2020/12/07 15:13
 */
@Data
public class ChannelContext {
    private String clientId;
    private String channelId;
    private ChannelHandlerContext channel;
    private String ip;
    private String type;
}
