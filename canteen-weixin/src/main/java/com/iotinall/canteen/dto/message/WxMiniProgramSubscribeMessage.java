package com.iotinall.canteen.dto.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 微信小程序订阅消息
 *
 * @author loki
 * @date 2020/09/24 21:07
 */
@Data
@Builder
public class WxMiniProgramSubscribeMessage implements Serializable {
    @JSONField(name = "touser")
    private String openId;

    @JSONField(name = "template_id")
    private String templateId;

    @JSONField(name = "page")
    private String page;

    @JSONField(name = "miniprogram_state")
    private String miniprogramState;

    @JSONField(name = "lang")
    private String lang ;

    @JSONField(name = "data")
    private Map<String, WxMiniProgramSubscribeMessageData> data;
}
