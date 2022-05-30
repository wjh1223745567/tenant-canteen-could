package com.iotinall.canteen.netty.manager.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 解码器
 *
 * @author loki
 * @date 2020/06/02 14:02
 */
@Slf4j
public class TcpDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            if (in.readableBytes() > 0) {
                byte[] bytesReady = new byte[in.readableBytes()];
                in.readBytes(bytesReady);
                out.add(bytesReady);
            }
        } finally {

        }
    }
}
