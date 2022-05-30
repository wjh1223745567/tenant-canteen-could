package com.iotinall.canteen.controller;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.hikvision.HicCallbackReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 海康威视闸机
 */
@Slf4j
@RestController
@RequestMapping(value = "hic_vision_device")
public class HicVisionDeviceController {

    /**
     * 回调事件
     */
    @PostMapping(value = "hic_callback")
    public void hicCallBack(@RequestBody HicCallbackReq req) {
        log.info("接收到海康威视闸机事件：{}", JSON.toJSONString(req));
    }

    /**
     * 智慧餐厅添加通行设备获取海康闸机列表
     */
    @PostMapping(value = "all")
    public ResultDTO all(@RequestParam(value = "keywords", required = false) String keywords) {
        return ResultDTO.success();
    }
}
