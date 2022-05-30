package com.iotinall.canteen.context.handler;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.constant.TcpConstants;
import com.iotinall.canteen.context.TcpClientContext;
import com.iotinall.canteen.netty.manager.factory.ChannelFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 光盘行动客户端接入逻辑
 *
 * @author loki
 * @date 2021/6/28 14:22
 **/
@Slf4j
@Service
public class EmptyPlateScreenHandler extends TcpClientHandlerWrapper {

    @Override
    public void handle(TcpClientContext context) {
        log.info("添加新请求：{}", JSON.toJSONString(context));
        ChannelFactory.addChannel(TcpConstants.CLIENT_TYPE_EMPTY_PLATE, context.getClient().getClientKey(), context.getChannelContext());

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
