package com.iotinall.canteen.dto.aibox;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * AIBox返回结果对象
 *
 * @author loki
 * @date 2021/7/20 14:43
 **/
@Data
@Accessors(chain = true)
@ApiModel(description = "AIBox返回结果对象")
public class AIBoxDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "name", required = true)
    private String name;

    private String deviceNo;

    private String remark;

    @ApiModelProperty(value = "详细位置", required = true)
    private String areaName;

    @ApiModelProperty(value = "安装位置", required = true)
    private Long tenantOrgId;

    @ApiModelProperty(value = "设备IP")
    private String ip;
}