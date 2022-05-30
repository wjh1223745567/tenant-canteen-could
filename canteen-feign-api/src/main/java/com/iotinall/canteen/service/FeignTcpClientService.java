package com.iotinall.canteen.service;

import com.iotinall.canteen.constants.FreshenMessEnum;
import com.iotinall.canteen.dto.tcpclient.FeignTcpClientDTO;
import com.iotinall.canteen.dto.tcpclient.TcpMsgDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(value = "canteen-tcp", contextId = "canteen-tcp")
public interface FeignTcpClientService {

    @GetMapping(value = "/tcp-client/feign/findById/{id}")
    FeignTcpClientDTO findById(@PathVariable("id") Long id);

    @PostMapping(value = "tcp-msg/send")
    void send(@RequestBody TcpMsgDTO msg, @RequestHeader(value = "token") String token);


    /**
     * 发送刷新看板消息
     * @param dataSource 数据源key
     * @param type 消息类型
     */
    @PostMapping(value = "tcp-msg/send/feign/sendFreshenMess")
    void sendFreshenMess(@RequestParam(value = "dataSource") String dataSource, @RequestParam(value = "type") Set<FreshenMessEnum> type);
}
