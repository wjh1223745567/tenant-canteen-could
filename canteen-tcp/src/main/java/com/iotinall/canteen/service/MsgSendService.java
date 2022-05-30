package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.entity.MsgSendRecord;
import com.iotinall.canteen.entity.TcpClient;
import com.iotinall.canteen.netty.service.WebSocketHelper;
import com.iotinall.canteen.repository.MsgSendRepository;
import com.iotinall.canteen.repository.TcpClientRepository;
import com.iotinall.canteen.dto.tcpclient.TcpMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * 客户端 service
 *
 * @author loki
 * @date 2021/04/14 14:34
 */
@Slf4j
@Service
public class MsgSendService {
    @Resource
    private TcpClientRepository tcpClientRepository;
    @Resource
    private MsgSendRepository msgSendRepository;

    /**
     * 发送消息
     *
     * @author loki
     * @date 2021/7/7 17:44
     **/
    public void send(TcpMsgDTO msg) {
        if (!CollectionUtils.isEmpty(msg.getClientIds())) {
            TcpClient client;
            MsgSendRecord record;
            for (Long clientId : msg.getClientIds()) {
                client = this.tcpClientRepository.findById(clientId).orElse(null);
                if (null != client) {
                    WebSocketHelper.sendByClientId(client.getClientKey(), msg.getData());

                    //保存发送记录
                    record = new MsgSendRecord();
                    record.setClientId(clientId);
                    record.setMsg(JSON.toJSONString(msg.getData()));
                    record.setSendType(msg.getSendByWebsocket() ? "websocket" : "tcp");
                    this.msgSendRepository.save(record);
                }
            }
        }
    }
}
