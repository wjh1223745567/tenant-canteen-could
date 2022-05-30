package com.iotinall.canteen.netty.manager.handler;

import com.iotinall.canteen.common.util.SpringContextUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * HTTP与TCP决策类
 *
 * @author loki
 * @date 2020/06/04 14:17
 */
@Slf4j
public class ProtocolDetermineHandler extends ByteToMessageDecoder {
    private static final int MAX_LENGTH = 23;
    private static final String WEBSOCKET = "GET /ws/";
    private static final String HTTP_GET = "GET /";
    private static final String HTTP_POST = "POST /";


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        PipelineChangeHandler pipelineChangeHandler = SpringContextUtil.getBean(PipelineChangeHandler.class);

        String protocol = getBufStart(in);
        log.info("接收到新连接：{}", protocol);

        if (protocol.startsWith(WEBSOCKET)) {
            pipelineChangeHandler.websocketPipeline(ctx.pipeline());
        } else if (protocol.startsWith(HTTP_GET) || protocol.startsWith(HTTP_POST)) {
            pipelineChangeHandler.httpPipeline(ctx.pipeline());
        } else {
            pipelineChangeHandler.tcpPipeline(ctx.pipeline());
        }

        in.resetReaderIndex();
        ctx.pipeline().remove(this.getClass());
    }

    /**
     * 读取请求开始字节流
     *
     * @author loki
     * @date 2019-08-13 20:28
     */
    private String getBufStart(ByteBuf in) {
        int length = in.readableBytes();
        if (length > MAX_LENGTH) {
            length = MAX_LENGTH;
        }

        // 标记读位置
        in.markReaderIndex();
        byte[] content = new byte[length];
        in.readBytes(content);
        return new String(content);
    }
}
