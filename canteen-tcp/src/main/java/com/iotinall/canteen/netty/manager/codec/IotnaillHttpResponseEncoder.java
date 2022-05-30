package com.iotinall.canteen.netty.manager.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.List;

/**
 * @author loki
 * @date 2020/06/02 16:19
 */
public class IotnaillHttpResponseEncoder extends HttpResponseEncoder {

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
