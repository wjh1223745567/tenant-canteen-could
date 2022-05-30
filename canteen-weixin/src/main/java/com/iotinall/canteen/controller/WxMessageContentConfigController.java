package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.sendmessage.SendMessageAddReq;
import com.iotinall.canteen.dto.sendmessage.SendMessageEditReq;
import com.iotinall.canteen.service.WxMessageContentConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "小程序信息推送配置", tags = "小程序信息推送配置")
@RestController
@RequestMapping("sys/send_message_config")
public class WxMessageContentConfigController {

    @Resource
    private WxMessageContentConfigService wxMessageContentConfigService;

    @GetMapping("list")
    @ApiOperation(value = "推送配置列表")
    public ResultDTO<?> list() {
        return ResultDTO.success(this.wxMessageContentConfigService.list());
    }

    @PostMapping
    @ApiOperation(value = "添加推送配置")
    public ResultDTO<?> add(@Validated @RequestBody SendMessageAddReq req) {
        this.wxMessageContentConfigService.add(req);
        return ResultDTO.success();
    }

    @PutMapping
    @ApiOperation(value = "修改推送配置")
    public ResultDTO<?> edit(@Validated @RequestBody SendMessageEditReq req) {
        this.wxMessageContentConfigService.edit(req);
        return ResultDTO.success();
    }

//    @DeleteMapping("{id}")
    @GetMapping(value = "deleted")
    @ApiOperation(value = "删除推送配置")
    public ResultDTO<?> del(Long[] id) {
        return ResultDTO.success(this.wxMessageContentConfigService.del(id));
    }
}
