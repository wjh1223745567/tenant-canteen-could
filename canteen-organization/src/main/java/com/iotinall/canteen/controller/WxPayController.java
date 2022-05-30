package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.service.SerMoneybagService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 支付密码
 * @author WJH
 * @date 2019/11/259:46
 */
@Api(tags = SwaggerModule.MODULE_PAY)
@RestController
@RequestMapping(value = "wx_tack_out_pay")
public class WxPayController {

    @Resource
    private SerMoneybagService serMoneybagService;

    /**
     * 设置支付密码
     * @param payPassword
     * @return
     */
    @GetMapping(value = "tackOutWxPayPayPassword")
    public ResultDTO tackOutWxPayPayPassword(@RequestParam String payPassword, String oldPayPassword){
        this.serMoneybagService.tackOutWxPayPayPassword(payPassword, oldPayPassword);
        return ResultDTO.success();
    }
}
