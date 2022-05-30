package com.iotinall.canteen.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Map;

@Getter
@ApiModel(value = "消息类型 早餐提醒 午餐提醒 晚餐提醒 余额不足提示")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SendMessageEnum {

    /**
     * 消息消息配置
     * 1-早餐
     * 2-午餐
     * 3-晚餐
     * 4-余额不足
     * 5-点评和饮食记录
     * 6-商城取货
     * 7-反馈
     * 8-进销存采购入库申请
     * 9-进销存采购退货申请
     * 10-进销存领用出库申请
     * 11-进销存领用退库申请
     * 12-进销存采购入库审核
     * 13-进销存采购退货审核
     * 14-进销存领用出库审核
     * 15-进销存领用退库审核
     */
    BREAKFAST(1, "早餐提醒"),
    LUNCH(2, "午餐提醒"),
    DINNER(3, "晚餐提醒"),
    BALANCE(4, "余额不足提醒"),
    COMMENTS(5, "点评和饮食记录提醒"),
    SHOPPING(6, "商城取货提醒"),
    FEEDBACK(7, "意见反馈提醒"),
    STOCK_IN_APPLY(8, "进销存采购入库申请"),
    STOCK_IN_BACK_APPLY(9, "进销存采购退货申请"),
    STOCK_OUT_APPLY(10, "进销存领用出库申请"),
    STOCK_OUT_BACK_APPLY(11, "进销存领用退库申请"),
    STOCK_IN_APPLY_AUDIT(12, "进销存采购入库审核"),
    STOCK_IN_BACK_APPLY_AUDIT(13, "进销存采购退货审核"),
    STOCK_OUT_APPLY_AUDIT(14, "进销存领用出库审核"),
    STOCK_OUT_BACK_APPLY_AUDIT(15, "进销存领用退库审核");

    @JsonProperty
    private final int code;
    @JsonProperty
    private final String desc;

    SendMessageEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * json creator方法，解决JsonFormat(shape=Object)的反序列化的坑
     *
     * @param value
     * @return
     */
    @JsonCreator
    public static SendMessageEnum creator(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return SendMessageEnum.valueOf(value.toString());
        }
        Integer code;
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            code = (Integer) map.get("code");
        } else {
            code = (Integer) value;
        }
        return SendMessageEnum.byCode(code);
    }

    public static SendMessageEnum byCode(int code) {
        for (SendMessageEnum type : SendMessageEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("not found SendMessageEnum by code:" + code);
    }

}
