package com.iotinall.canteen.controller;

import com.github.wxpay.sdk.WXXmlConstants;
import com.iotinall.canteen.properties.WXPayProperties;
import com.iotinall.canteen.service.WxService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 订单支付
 * @author WJH
 * @date 2019/11/616:17
 */
@RestController
@RequestMapping("pay")
public class PayController {
    @Resource
    private WxService wxService;

    @Resource
    private WXPayProperties wxpayProperties;

    @PostMapping("wxNotify")
    @ApiOperation("微信回调")
    public String wxPayNotify(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml);
            return result;
        } catch (Exception e) {
            System.out.println("微信手机支付失败:" + e.getMessage());
            return WXXmlConstants.PAY_FAILED_MSG;
        }
    }

}
