package com.iotinall.canteen.context;

import com.iotinall.canteen.constant.TcpConstants;
import com.iotinall.canteen.context.handler.*;
import com.iotinall.canteen.entity.TcpClient;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * tcp client context
 *
 * @author loki
 * @date 2021/6/28 14:34
 **/
@Data
public class TcpClientContext {
    private TcpClient client;
    private ChannelHandlerContext channelContext;

    public TcpClientContext(TcpClient client, ChannelHandlerContext channel) {
        this.client = client;
        this.channelContext = channel;
    }

    static Map<String, TcpClientHandler> handlers = new HashMap<>();

    static {
        handlers.put(TcpConstants.CLIENT_TYPE_BIG_SCREEN, new BigScreenHandler());
        handlers.put(TcpConstants.CLIENT_TYPE_COMMENT, new CommentMachineHandler());
        handlers.put(TcpConstants.CLIENT_TYPE_DINER_NUMBER, new GateNumberScreenHandler());
        handlers.put(TcpConstants.CLIENT_TYPE_EMPTY_PLATE, new EmptyPlateScreenHandler());
    }

    /**
     * 处理接入的客户端
     *
     * @author loki
     * @date 2021/6/28 14:41
     **/
    public void handle() {
        handlers.get(client.getType()).handle(this);
    }
}
