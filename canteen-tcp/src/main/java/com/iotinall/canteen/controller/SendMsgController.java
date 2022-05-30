package com.iotinall.canteen.controller;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constants.FreshenMessEnum;
import com.iotinall.canteen.service.MsgSendService;
import com.iotinall.canteen.dto.tcpclient.TcpMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 消息发送
 *
 * @author loki
 * @date 2021/6/25 11:04
 **/
@Slf4j
@RestController
@RequestMapping("tcp-msg/send")
public class SendMsgController {
    @Resource
    private MsgSendService msgSendService;

    @PostMapping
    public ResultDTO send(@RequestBody TcpMsgDTO msg) {
        msgSendService.send(msg);
        return ResultDTO.success();
    }

    /**
     * 发送刷新看板消息
     * @param dataSource 数据源key
     * @param type 消息类型
     */
    @PostMapping(value = "feign/sendFreshenMess")
    public void sendFreshenMess(@RequestParam(value = "dataSource") String dataSource, @RequestParam(value = "type") Set<FreshenMessEnum> type){
        log.info("发送消息：{}，{}", dataSource, JSON.toJSONString(type));
    }
}
