package com.iotinall.canteen.dto.attendance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "设备添加请求参数", description = "设备添加请求参数")
@Data
public class EquipmentAddReq implements Serializable {

    @ApiModelProperty(value = "设备ID，编辑必传")
    private Long id;

    @ApiModelProperty(value = "设备名称", required = true)
    @NotBlank(message = "设备名称不能为空")
    private String name;

    @ApiModelProperty(value = "设备编号", required = true)
    @NotBlank(message = "设备编码不能为空")
    private String deviceNo;

    @ApiModelProperty(value = "设备ip")
    private String ip;

    @ApiModelProperty(value = "人脸库", required = true)
    @NotNull(message = "人脸库不能为空")
    private Long empLib;

    /**
     * 安装位置
     */
    private Long tenantOrgId;

    /**
     * 详细位置
     */
    private String areaName;
}
