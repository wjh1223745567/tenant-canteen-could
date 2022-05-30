package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 新开普接口统一返回
 *
 * @author loki
 * @date 2021/6/21 11:07
 **/
@Data
@Accessors(chain = true)
public class ReqDTO implements Serializable {
    /**
     * 应用ID，一卡通系统分配
     */
    @JsonProperty(value = "appid")
    private String appId;

    /**
     * 返回结果：200成功，其他失败
     */
    @JsonProperty(value = "dpcode")
    private String dpCode;

    /**
     * 业务结果，json格式字符串
     */
    private String data;

    /**
     * 请求日期 yyyyMMddHHmmss
     */
    @JsonProperty(value = "ctdate")
    private String ctDate;

    /**
     * 消息ID，每次请求保证唯一，建议用UUID
     */
    @JsonProperty(value = "msgid")
    private String msgId;

    /**
     * 签名值
     */
    private String sign;
}
