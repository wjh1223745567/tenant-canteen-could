package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.github.wxpay.sdk.WXXmlConstants;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.properties.WXPayProperties;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.MchPayApp;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.util.PayUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WJH
 * @date 2019/11/616:19
 */
@Service
@Slf4j
public class WxService {

    @Resource
    private WXPayProperties wxpayProperties;

    @Resource
    private FeignSerMoneybagService feignSerMoneybagService;

    @GlobalTransactional(rollbackFor = Exception.class)
    public String payBack(String resXml) throws Exception {
        WXPay wxpay = new WXPay(wxpayProperties);
        Map<String, String> notifyMap;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                String return_code = notifyMap.get(WXPayConstants.FIELD_RETURN_CODE);
                String out_trade_no = notifyMap.get("out_trade_no");
                if (WXPayConstants.SUCCESS.equals(return_code)) {
                    if (out_trade_no != null) {
                        feignSerMoneybagService.addBalanceResult(out_trade_no, true);
                        log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                        return WXXmlConstants.PAY_SUCCESS_MSG;
                    } else {
                        log.error("微信回调信息:{}", resXml);
                        return WXXmlConstants.PAY_FAILED_MSG;
                    }
                }else{
                    this.feignSerMoneybagService.addBalanceResult(out_trade_no, false);
                    return WXXmlConstants.PAY_FAILED_MSG;
                }
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                log.error("手机支付回调通知签名错误");
                return WXXmlConstants.PAY_FAILED_MSG;
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败", e);
            e.printStackTrace();
            return WXXmlConstants.PAY_FAILED_MSG;
        }
    }

    public Map<String, String> doUnifiedOrder(String orderNo,  String productName, BigDecimal amount, String oppenid){
        try {
            WXPay wxPay = new WXPay(wxpayProperties);
            Map<String, String> data = new HashMap<>();
            //生成商户订单号，不可重复
            data.put("appid", wxpayProperties.getAppId());
            data.put("mch_id", wxpayProperties.getMchId());
            data.put("nonce_str", WXPayUtil.generateNonceStr());
            data.put("body", productName);
            data.put("out_trade_no", orderNo);
            data.put("total_fee", String.valueOf(amount.multiply(BigDecimal.valueOf(100L)).intValue()));
            data.put("openid", oppenid);
            //自己的服务器IP地址
            data.put("spbill_create_ip", InetAddress.getLocalHost().getHostAddress());
            //异步通知地址（请注意必须是外网）
            data.put("notify_url", wxpayProperties.getNotifyUrl());
            //交易类型
            data.put("trade_type", wxpayProperties.getTradeType());
            //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
            data.put("sign", WXPayUtil.generateSignature(data, wxpayProperties.getKey(), WXPayConstants.SignType.MD5));
            //使用官方API请求预付订单
            Map<String, String> response = wxPay.unifiedOrder(data);

            if (WXPayConstants.SUCCESS.equals(response.get(WXPayConstants.FIELD_RETURN_CODE))
                    && !WXPayConstants.FAIL.equals(response.get(WXPayConstants.FIELD_RESULT_CODE))) {//主要返回以下5个参数
                Map<String, String> param = new HashMap<>();
                param.put("appId",wxpayProperties.getAppId());
                param.put("timeStamp",System.currentTimeMillis()/1000+"");
                param.put("nonceStr", response.get("nonce_str"));
                param.put("package","prepay_id="+response.get("prepay_id"));
                param.put("signType","MD5");
                param.put("paySign",WXPayUtil.generateSignature(param, wxpayProperties.getKey(), WXPayConstants.SignType.MD5));
                log.info("支付参数：{}", JSON.toJSONString(param));
                return param;
            }else{
                log.error("下单失败：{},{}",response.get("return_msg"), response.get("err_code_des"));
                throw new BizException("","下单失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("","下单失败");
        }
    }

    /**
     * 外带外购微信支付
     * @param orderNo 订单号
     * @param productName 产品名称
     * @param amount 金额
     * @param oppenid openid
     * @return MchPayApp
     */
    public MchPayApp doTakeOutOrder(String orderNo, String productName, BigDecimal amount, String oppenid) {
        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(wxpayProperties.getAppId());
        unifiedorder.setMch_id(wxpayProperties.getMchId());
        unifiedorder.setNonce_str(WXPayUtil.generateNonceStr());
        unifiedorder.setOut_trade_no(orderNo);
        unifiedorder.setTotal_fee( String.valueOf(amount.multiply(BigDecimal.valueOf(100L)).intValue()));
        unifiedorder.setOpenid(oppenid);
        try {
            unifiedorder.setSpbill_create_ip(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        unifiedorder.setNotify_url(wxpayProperties.getTackOutNotifyUrl());
        unifiedorder.setTrade_type(wxpayProperties.getTradeType());
        UnifiedorderResult unifiedorderResult = PayMchAPI.payUnifiedorder(unifiedorder, wxpayProperties.getKey());
        if(unifiedorderResult.getSign_status() != null && unifiedorderResult.getSign_status()){
            log.info("微信支付验签成功");
            return PayUtil.generateMchAppData(unifiedorderResult.getPrepay_id(), wxpayProperties.getAppId(), wxpayProperties.getMchId(), wxpayProperties.getKey());
        }else{
            throw new BizException("","微信下单失败");
        }
    }

}
