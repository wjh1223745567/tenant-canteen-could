package com.iotinall.canteen.dto.emptyplate;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 光盘行动消息推送配置
 *
 * @author loki
 * @date 2021/7/7 11:16
 **/
@Data
public class EmptyPlateMsgConfigAddReq implements Serializable {
    @NotNull(message = "请选择摄像机")
    private Long cameraId;

    @NotNull(message = "请选择通知客户端")
    private Long clientId;

    @NotBlank(message = "请输入名称")
    private String name;
}
