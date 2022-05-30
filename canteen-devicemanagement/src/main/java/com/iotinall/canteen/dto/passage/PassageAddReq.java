package com.iotinall.canteen.dto.passage;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 通行设备添加请求参数
 *
 * @author loki
 * @date 2021/7/9 12:03
 **/
@Data
public class PassageAddReq implements Serializable {
    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    private String name;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空")
    private String deviceNo;

    /**
     * 详细位置
     */
    private String areaName;

    /**
     * 租户组织ID
     */
    @NotNull(message = "安装位置不能为空")
    private Long tenantOrgId;

    /**
     * 备注
     */
    private String remark;
}
