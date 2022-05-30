package com.iotinall.canteen.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Map;

/**
 * 账单类型
 *
 * @author WJH
 * @date 2019/11/514:32
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@ApiModel(value = "标题 0 早餐 1午餐 2晚餐 3下午茶 4微信充值 5支付宝充值")
public enum BillType {

    BREAKFAST(0, "早餐"),
    LUNCH(1, "午餐"),
    DINNER(2, "晚餐"),
    AFTERNOONTEA(3, "下午茶"),
    WECHAT(4, "微信充值"),
    ALIPAY(5, "支付宝充值"),
    WALLETPAYMENT(6, "网上商城"),
    SYSTEM(7, "后台充值");

    private Integer code;

    private String name;

    BillType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @JsonCreator
    public static BillType creator(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return BillType.valueOf(value.toString());
        }
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Integer code = (Integer) map.get("code");
            return BillType.values()[code]; // code 假定就是
        }
        Integer code = (Integer) value;
        return BillType.values()[code];
    }

    public static BillType getByCode(Integer code) {
        BillType billType = null;
        try {
            billType = BillType.values()[code];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return billType;
    }
}
