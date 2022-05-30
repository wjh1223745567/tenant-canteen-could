package com.iotinall.canteen.netty.manager.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消息处理器
 *
 * @author loki
 * @date 2021/01/09 16:37
 */
@Slf4j
@ChannelHandler.Sharable
@Service
public class TcpHandler extends ChannelInboundHandlerAdapter {

    public static ApplicationContext applicationContext;

    /**
     * 读取消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = new String((byte[]) msg);
        log.info("收到消息：{}", message.length() > 2000 ? message.length() : message);
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.channelRead(ctx, msg);
    }

    @Resource
    public void setApplicationContext(ApplicationContext applicationContext) {
        TcpHandler.applicationContext = applicationContext;
    }
}
