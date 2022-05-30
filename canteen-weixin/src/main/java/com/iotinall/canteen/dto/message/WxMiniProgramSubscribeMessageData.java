package com.iotinall.canteen.dto.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信小程序订阅消息
 *
 * @author loki
 * @date 2020/09/24 21:07
 */
@Data
public class WxMiniProgramSubscribeMessageData implements Serializable {
    @JSONField(name = "value")
    private String value;

    public WxMiniProgramSubscribeMessageData(String value) {
        this.value = value;
    }
}
