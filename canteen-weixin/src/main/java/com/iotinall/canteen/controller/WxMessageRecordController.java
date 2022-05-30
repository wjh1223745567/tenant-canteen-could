package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.WxMessageSendRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author loki
 * @date 2021/04/17 18:01
 */
@Api(value = "小程序信息记录", tags = "小程序信息记录")
@RestController
@RequestMapping("wx/message")
public class WxMessageRecordController {

    @Resource
    private WxMessageSendRecordService wxMessageSendRecordService;

    @ApiOperation(value = "消息列表")
    @GetMapping
    public ResultDTO list(@PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable page) {
        return ResultDTO.success(this.wxMessageSendRecordService.getAppMessageRecordPage(page));
    }
}
