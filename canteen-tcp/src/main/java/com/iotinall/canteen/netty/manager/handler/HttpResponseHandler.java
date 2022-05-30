package com.iotinall.canteen.netty.manager.handler;


import com.iotinall.canteen.netty.manager.factory.HttpResponseFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 设备返回对象处理器，目前只有推送记录
 *
 * @author loki
 * @date 2020/06/02 15:58
 */
@Slf4j
public class HttpResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof FullHttpResponse) {
                FullHttpResponse response = (FullHttpResponse) msg;
                HttpResponseFactory.responseHandle(ctx, response);
            } else if (msg instanceof FullHttpRequest) {
                log.info("收到信息：{}", ((FullHttpRequest) msg).content().toString(CharsetUtil.UTF_8));
                //FaceRecordThread.msgQueue.offer(new ReceivedMsgDTO().setData(((FullHttpRequest) msg).content().toString(CharsetUtil.UTF_8)));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
