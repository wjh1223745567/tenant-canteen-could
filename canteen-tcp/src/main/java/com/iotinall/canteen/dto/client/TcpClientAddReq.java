package com.iotinall.canteen.dto.client;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * tcp client 添加请求参数
 *
 * @author loki
 * @date 2021/6/25 11:20
 **/
@Data
public class TcpClientAddReq implements Serializable {
    /**
     * 客户端唯一标志
     */
    @NotBlank(message = "唯一值key不能为空")
    private String clientKey;

    /**
     * 客户端名称
     */
    @NotBlank(message = "名称不能为空")
    private String clientName;

    /**
     * 客户端IP
     */
    private String ip;

    /**
     * 客户端类型
     */
    @NotBlank(message = "终端类型不能为空")
    private String type;

    /**
     * 租户ID
     */
    @NotNull(message = "安装位置不能为空")
    private Long tenantOrgId;

    /**
     * 安装位置
     */
    @NotBlank(message = "详细位置不能为空")
    private String areaName;

    private String remark;
}
