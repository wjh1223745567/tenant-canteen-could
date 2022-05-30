package com.iotinall.canteen.context.handler;

import com.iotinall.canteen.context.TcpClientContext;

public interface TcpClientHandler {
    /**
     * 首次接入消息处理
     *
     * @author loki
     * @date 2021/7/8 10:12
     **/
    void handle(TcpClientContext context);
}
