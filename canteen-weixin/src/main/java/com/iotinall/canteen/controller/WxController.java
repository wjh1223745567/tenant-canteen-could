package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 微信相关的controller
 *
 * @author loki
 * @date 2020/09/27 11:09
 */
@Api(tags = "微信相关的controller")
@RestController
@RequestMapping(value = "wx")
public class WxController {
    @Autowired
    private WxMessageSendService wxMessageSendService;
    @Autowired
    private UserTemplateSubRelationService userTemplateSubRelationService;
    @Autowired
    private MiniProgramSubscriptionService miniProgramSubscriptionService;
    @Autowired
    private WxContentCheckService wxContentCheckService;

    @Resource
    private WxService wxService;

    @ApiOperation(value = "获取微信小程序appId", httpMethod = "GET")
    @GetMapping(value = "/app-id")
    public ResultDTO getWxAppSecret() {
        return ResultDTO.success(wxMessageSendService.getAppSecret());
    }

    @ApiOperation(value = "获取微信小程序订阅消息模板", httpMethod = "GET")
    @GetMapping(value = "/miniprogram/sub/template")
    public ResultDTO getMiniProgramSubTemplate(@RequestParam("type") @ApiParam(value = "小程序订阅消息类型 STOCK_BILL_APPLY:申请 STOCK_BILL_AUDIT:审核 DINING_INFORMATION:就餐") String type) {
        return ResultDTO.success(miniProgramSubscriptionService.getMiniProgramSubTemplate(type));
    }

    @ApiOperation(value = "保存用户订阅的小程序消息", httpMethod = "POST")
    @PostMapping(value = "/user/sub")
    public ResultDTO saveUserSubTemplate(@RequestParam(value = "templateIds", required = false) String[] templateIds) {
        if (templateIds.length > 0) {
            userTemplateSubRelationService.saveUserSubTemplate(templateIds);
        }
        return ResultDTO.success();
    }

    @ApiOperation(value = "微信敏感词校验", httpMethod = "GET")
    @GetMapping(value = "/content/check")
    public ResultDTO check(@RequestParam("content") String content) {
        return ResultDTO.success(wxContentCheckService.check(content));
    }

    @PostMapping(value = "feign/doUnifiedOrder")
    public Map<String, String> doUnifiedOrder(@RequestParam(value = "orderNo") String orderNo, @RequestParam(value = "productName") String productName, @RequestParam(value = "amount") BigDecimal amount, @RequestParam(value = "oppenid") String oppenid){
        return wxService.doUnifiedOrder(orderNo, productName, amount, oppenid);
    }
}
