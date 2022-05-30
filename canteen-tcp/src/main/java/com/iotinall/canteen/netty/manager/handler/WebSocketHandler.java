package com.iotinall.canteen.netty.manager.handler;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.context.TcpClientContext;
import com.iotinall.canteen.entity.TcpClient;
import com.iotinall.canteen.netty.manager.factory.ChannelFactory;
import com.iotinall.canteen.service.TcpClientService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * websocket处理类
 * 1、餐厅大屏
 * 2、餐厅点评机器
 *
 * @author loki
 * @date 2020/11/20 18:18
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static TcpClientService tcpClientService;

    static {
        tcpClientService = SpringContextUtil.getBean(TcpClientService.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            this.handleRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;

            //收到客户端消息回复消息
            if (StringUtils.isNotBlank(frame.text())) {
                log.info("收到心跳信息：{}", frame.text());
                heartbeat(ctx);
                //ctx.channel().writeAndFlush(new TextWebSocketFrame("pong"));
            }

            return;
        } else if (msg instanceof CloseWebSocketFrame) {
            ChannelFactory.removeChannelByChannelId(ctx.channel());
            return;
        }

        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        ChannelFactory.removeChannelByChannelId(ctx.channel());
        log.info("websocket 掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ChannelFactory.removeChannelByChannelId(ctx.channel());
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 处理请求
     *
     * @author loki
     * @date 2020/11/21 11:47
     */
    private void handleRequest(ChannelHandlerContext context, HttpRequest request) {
        String clientId = request.uri().substring(1).split("/")[1];
        log.info("收到新TCP-CLIENT:{}", clientId);

        if (StringUtils.isBlank(clientId)) {
            throw new BizException("未知TCP CLIENT");
        }

        //客户端是否在系统中注册
        TcpClient tcpClient = tcpClientService.getTcpClient(clientId);
        if (null == tcpClient) {
            throw new BizException("未知TCP CLIENT");
        }

        handshake(request, context);

        new TcpClientContext(tcpClient, context).handle();
    }

    /**
     * 握手
     *
     * @author loki
     * @date 2021/6/28 13:58
     **/
    private void handshake(HttpRequest request, ChannelHandlerContext context) {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(request.uri(), null, false);
        wsFactory.newHandshaker(request).handshake(context.channel(), request);
    }

    /**
     * 心跳
     *
     * @author loki
     * @date 2021/7/8 10:05
     **/
    private void heartbeat(ChannelHandlerContext context) {
        TcpClient tcpClient = tcpClientService.getTcpClient(ChannelFactory.getClientIdByChannelId(context));
        if (null == tcpClient) {
            throw new BizException("未知TCP CLIENT");
        }

        tcpClientService.heartbeat(tcpClient);
    }
}
