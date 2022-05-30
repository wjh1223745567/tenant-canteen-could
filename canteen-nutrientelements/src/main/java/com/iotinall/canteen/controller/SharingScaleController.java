package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.SharingScaleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 共享秤对接
 */
@RestController
@RequestMapping(value = "sharing_scale")
public class SharingScaleController {

    @Resource
    private SharingScaleService sharingScaleService;

    /**
     * 接收数据
     * @return
     */
    @GetMapping(value = "receive_data")
    public ResultDTO receiveData(@RequestParam(value = "data") String data){
        this.sharingScaleService.receiveData(data);
        return ResultDTO.success();
    }

}
