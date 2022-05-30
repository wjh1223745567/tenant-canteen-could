package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 新开普接口统一返回
 *
 * @author loki
 * @date 2021/6/21 11:07
 **/
@Data
public class RespDTO implements Serializable {
    /**
     * 应用ID，一卡通系统分配
     */
    @JsonProperty(value = "appid")
    private String appId;

    /**
     * 返回结果：200成功，其他失败
     */
    @JsonProperty(value = "retcode")
    private Integer retCode;

    /**
     * 返回描述
     */
    @JsonProperty(value = "retmsg")
    private String retMsg;

    /**
     * 业务结果，json格式字符串
     */
    private String data;

    /**
     * 签名值
     */
    private String sign;
}
