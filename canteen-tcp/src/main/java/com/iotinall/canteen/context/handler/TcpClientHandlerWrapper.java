package com.iotinall.canteen.context.handler;

import com.iotinall.canteen.context.TcpClientContext;
import org.springframework.stereotype.Service;

/**
 * 包装类
 *
 * @author loki
 * @date 2021/7/8 10:16
 **/
@Service
public class TcpClientHandlerWrapper implements TcpClientHandler {
    @Override
    public void handle(TcpClientContext context) {

    }
}
