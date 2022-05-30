package com.iotinall.canteen.dto.syspermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 *
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
@Data
@ApiModel(description = "返回系统权限结果")
public class SysPermissionDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;// id

    @ApiModelProperty(value = "权限名", required = true)
    private String name;// 权限名

    @ApiModelProperty(value = "权限")
    private String permission;// 权限

    @ApiModelProperty(value = "父权限")
    private Long pid;// 父权限
}