package com.iotinall.canteen.dto.tcpclient;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * TCP 消息
 *
 * @author loki
 * @date 2021/7/7 17:11
 **/
@Data
@Accessors(chain = true)
public class TcpMsgDTO implements Serializable {
    /**
     * 需要通知的客户端
     */
    private List<Long> clientIds;

    /**
     * 消息内容
     */
    private Object data;

    /**
     * 是否通过websocket发送
     */
    private Boolean sendByWebsocket = true;
}
