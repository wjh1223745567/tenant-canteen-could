package com.iotinall.canteen.dto.aibox;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加equ_face_device请求
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
@Data
@ApiModel(description = "添加请求参数")
public class AIBoxAddReq implements Serializable {
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "请填写名称")
    private String name;

    @ApiModelProperty(value = "安装位置", required = true)
    @NotBlank(message = "请输入安装位置")
    private String areaName;

    @ApiModelProperty(value = "设备编码")
    private String deviceNo;

    @ApiModelProperty(value = "安装位置")
    private Long tenantOrgId;

    @ApiModelProperty(value = "设备ip")
    private String ip;

    @ApiModelProperty(value = "备注")
    private String remark;
}