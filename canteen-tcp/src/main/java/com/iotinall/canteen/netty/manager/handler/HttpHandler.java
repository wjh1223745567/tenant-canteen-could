package com.iotinall.canteen.netty.manager.handler;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.constant.NettyConstants;
import com.iotinall.canteen.netty.manager.factory.ChannelFactory;
import com.iotinall.canteen.netty.manager.factory.HttpResponseFactory;
import com.iotinall.canteen.netty.pojo.equip.heart.HeartBack;
import com.iotinall.canteen.netty.pojo.equip.heart.HeartReportData;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;


/**
 * 设备心跳处理
 *
 * @author loki
 * @date 2020/06/02 15:58
 */
@Slf4j
public class HttpHandler extends SimpleChannelInboundHandler {
    public volatile String serialNo;
    private final String REQUEST_FROM_KQ_DEVICE = "/LAPI/V1.0/PACS/Controller/HeartReportInfo";
    private final String REQUEST_FROM_GATE_PASS = "/GATE-PASS/";

    /**
     * 读取通道,过滤并处理心跳消息
     *
     * @author loki
     * @date 2020/06/02 16:38
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        String ip = ChannelFactory.getChannelIp(ctx);
        try {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest httpRequest = (FullHttpRequest) msg;
                String uri = httpRequest.uri();

            } else {
                ctx.fireChannelRead(msg);
            }
        } catch (Exception e) {
            log.error("Read [{}] channel msg[{}] failed for:{}", ip, JSON.toJSONString(msg), e.getMessage(), e);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 心跳回包
     */
    private void getHeartInfo(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(getHeartBackByteBuff());
    }

    /**
     * 心跳回包拼装
     *
     * @return
     */
    private FullHttpResponse getHeartBackByteBuff() {
        HeartBack heartBack = new HeartBack();
        HeartReportData data = new HeartReportData();
        heartBack.setData(data);
        return buildResponse(heartBack);
    }

    /**
     * 构建简单返回对象
     *
     * @author loki
     * @date 2021/01/20 11:31
     */
    private FullHttpResponse buildSimpleResponse(Object data) {
//        SyncResponse response = new SyncResponse();
//        response.setData(data);
//
//        return buildResponse(response);

        return null;
    }

    /**
     * 构建请求返回对象
     *
     * @author loki
     * @date 2021/01/11 20:22
     */
    private FullHttpResponse buildResponse(Object data) {
        String strBody = JSON.toJSONString(data);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(strBody.getBytes()));
        response.headers().set(NettyConstants.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(NettyConstants.CONTENT_TYPE, "text/plain");
        response.headers().set(NettyConstants.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        response.headers().set(NettyConstants.X_FRAME_OPTIONS, "SAMEORIGIN");
        return response;
    }

    /**
     * 添加channel时
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        log.debug("SimpleChatClient:" + incoming.remoteAddress() + "通道被添加");
    }

    /**
     * 删除channel时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        log.debug("SimpleChatClient:" + incoming.remoteAddress() + "通道被删除");
    }

    /**
     * 服务端监听到客户端不活动
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        try {
            ChannelFactory.removeChannelByChannelId(ctx.channel());
        } catch (Exception ex) {
            log.info("移除通道失败");
        }
        log.error("设备掉线");
    }

    /**
     * 服务端监听到客户端活动
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //服务端接收到客户端上线通知
        Channel incoming = ctx.channel();
        log.debug("SimpleChatClient:" + incoming.remoteAddress() + "在线");
    }

    /**
     * 服务端监听到客户端活动
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        log.error("SimpleChatClient:{}", incoming.remoteAddress() + "异常 : " + cause);
        //出现异常判断下通道是否还可用
        if (!incoming.isWritable()) {
            ctx.close();
            HttpResponseFactory.removeResponse(ctx);
        }
    }
}
