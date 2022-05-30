package com.iotinall.canteen.netty.manager.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequestEncoder;

import java.util.List;

/**
 * 放大了netty请求编码器中编码方法的访问权限
 *
 * @author loki
 * @date 2020/06/02 16:19
 */
public class IotnaillHttpRequestEncoder extends HttpRequestEncoder {

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
