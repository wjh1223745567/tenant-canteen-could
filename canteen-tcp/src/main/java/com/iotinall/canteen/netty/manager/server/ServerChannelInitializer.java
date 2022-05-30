package com.iotinall.canteen.netty.manager.server;


import com.iotinall.canteen.constant.NettyConstants;
import com.iotinall.canteen.netty.manager.handler.ProtocolDetermineHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 消息处理
 *
 * @author loki
 * @date 2020/06/02 16:21
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(NettyConstants.READ_TIME_OUT, NettyConstants.READ_TIME_OUT, NettyConstants.READ_TIME_OUT, TimeUnit.SECONDS));
        pipeline.addLast("protocolDetermine", new ProtocolDetermineHandler());
    }
}
