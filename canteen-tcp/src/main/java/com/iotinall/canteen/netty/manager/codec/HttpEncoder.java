package com.iotinall.canteen.netty.manager.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 编码器
 *
 * @author loki
 * @date 2020/06/02 16:19
 */
@Slf4j
public class HttpEncoder<I> extends MessageToMessageEncoder {

    /**
     * request编码器
     */
    private IotnaillHttpRequestEncoder httpRequestEncoder = new IotnaillHttpRequestEncoder();

    /**
     * response编码器
     */
    private IotnaillHttpResponseEncoder httpResponseEncoder = new IotnaillHttpResponseEncoder();


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        if (msg instanceof HttpRequest) {
            httpRequestEncoder.encode(ctx, msg, out);
        } else if (msg instanceof HttpResponse) {
            httpResponseEncoder.encode(ctx, msg, out);
        } else {
            log.info("未知消息类型");
        }
    }
}
