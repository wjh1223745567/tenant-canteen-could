package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.HuaweiCameraService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 华为设备接口对接
 *
 * @author loki
 * @date 2021/6/29 16:28
 **/
@RestController
@RequestMapping("/device/huawei")
public class HuaWeiDeviceController {
    @Resource
    private HuaweiCameraService huaweiDeviceService;

    /**
     * 添加本地摄像头 关联华为摄像头
     *
     * @author loki
     * @date 2021/7/9 11:31
     **/
    @GetMapping("all")
    public ResultDTO all(@RequestParam(value = "keywords",required = false) String keywords) {
        return ResultDTO.success(huaweiDeviceService.all(keywords));
    }


    /**
     * 登入
     *
     * @author loki
     * @date 2021/6/29 16:34
     **/
    @PostMapping("login")
    public ResultDTO login() {
        huaweiDeviceService.login();
        return ResultDTO.success();
    }

    /**
     * 获取摄像机列表
     *
     * @author loki
     * @date 2021/6/29 16:34
     **/
    @GetMapping("camera/list")
    public ResultDTO cameraList() {
        huaweiDeviceService.getCameraList();
        return ResultDTO.success();
    }

    /**
     * 抓拍
     *
     * @author loki
     * @date 2021/6/29 16:34
     **/
    @GetMapping("camera/snapshot")
    public ResultDTO snapshot(@RequestParam("cameraCode") String cameraCode, @RequestParam("domainCode") String domainCode) {
        huaweiDeviceService.snapshot(cameraCode, domainCode);
        return ResultDTO.success();
    }

    /**
     * 获取抓拍图片
     *
     * @author loki
     * @date 2021/6/29 16:34
     **/
    @GetMapping("camera/snapshot/list")
    public ResultDTO snapshotList(@RequestParam("cameraCode") String cameraCode,
                                  @RequestParam("domainCode") String domainCode) {
        huaweiDeviceService.getSnapshotList(cameraCode, domainCode);
        return ResultDTO.success();
    }
}
