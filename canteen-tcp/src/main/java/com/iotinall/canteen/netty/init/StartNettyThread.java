package com.iotinall.canteen.netty.init;

import com.iotinall.canteen.netty.manager.factory.NettyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * NETTY启动
 *
 * @author loki
 * @date 2020/06/02 13:59
 */
@Slf4j
@Service
public class StartNettyThread implements Runnable {

    private Integer isKeepHttpConnectionPort;

    public void setIsKeepHttpConnectionPort(Integer isKeepHttpConnectionPort) {
        this.isKeepHttpConnectionPort = isKeepHttpConnectionPort;
    }

    @Override
    public void run() {
        try {
            new NettyFactory(isKeepHttpConnectionPort).createNetty();
        } catch (Exception e) {
            log.info(e.toString());
        }
    }
}
