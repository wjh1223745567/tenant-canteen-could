package com.iotinall.canteen.netty.manager.codec;

import com.iotinall.canteen.constant.NettyConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 编码器
 *
 * @author loki
 * @date 2020/06/02 14:02
 */
@Slf4j
public class TcpEncoder extends MessageToByteEncoder<byte[]> {
    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] bytes, ByteBuf out) {
        String msg = new String(bytes);
        log.debug("发送消息 {}", msg);
        try {
            out.writeBytes(bytes);
            out.writeBytes(NettyConstants.DEFAULT_MSG_DELIMITER.getBytes());
        } catch (Exception ex) {
            log.info("发送消息:{}失败", msg);
        }
    }
}
