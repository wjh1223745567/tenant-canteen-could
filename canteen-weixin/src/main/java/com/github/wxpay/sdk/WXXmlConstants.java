package com.github.wxpay.sdk;

import java.util.HashMap;
import java.util.Map;

public class WXXmlConstants {

    public static String PAY_SUCCESS_MSG;

    public static String PAY_FAILED_MSG;

    static {
        try {
            Map<String, String> map = new HashMap<>(2);
            map.put("return_code", WXPayConstants.SUCCESS);
            map.put("return_msg", WXPayConstants.OK);
            PAY_SUCCESS_MSG = WXPayUtil.mapToXml(map);

            map.put("return_code", WXPayConstants.FAIL);
            map.put("return_msg", "");
            PAY_FAILED_MSG = WXPayUtil.mapToXml(map);
        } catch (Exception e) {
            throw new RuntimeException("generate xml failed", e);
        }

    }
}
