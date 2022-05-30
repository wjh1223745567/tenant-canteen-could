package com.iotinall.canteen.dto.emptyplate;

import lombok.Data;

import java.io.Serializable;

/**
 * 光盘行动消息推送配置
 *
 * @author loki
 * @date 2021/7/7 11:16
 **/
@Data
public class EmptyPlateMsgConfigDTO implements Serializable {
    private Long id;

    /**
     * 摄像头ID
     */
    private Long cameraId;

    /**
     * 摄像头名称
     */
    private String cameraName;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 配置名称
     */
    private String name;
}
