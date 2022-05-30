package com.iotinall.canteen.context.handler;

import com.iotinall.canteen.context.TcpClientContext;
import com.iotinall.canteen.netty.manager.factory.ChannelFactory;
import org.springframework.stereotype.Service;

/**
 * 点评机客户端接入逻辑
 *
 * @author loki
 * @date 2021/6/28 14:22
 **/
@Service
public class CommentMachineHandler extends TcpClientHandlerWrapper {

    @Override
    public void handle(TcpClientContext context) {
        ChannelFactory.addChannel(context.getClient().getType(), context.getClient().getClientKey(), context.getChannelContext());

        sendData();
    }

    /**
     * 发送数据
     *
     * @author loki
     * @date 2021/6/28 14:51
     **/
    public void sendData() {

    }
}
