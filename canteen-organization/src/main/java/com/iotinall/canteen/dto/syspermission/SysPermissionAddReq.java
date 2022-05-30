package com.iotinall.canteen.dto.syspermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 添加系统权限请求
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
@Data
@ApiModel(description = "添加系统权限请求")
public class SysPermissionAddReq {

    @ApiModelProperty(value = "权限名", required = true)
    @NotBlank(message = "请填写权限名")
    private String name;// 权限名

    @ApiModelProperty(value = "权限")
    private String permission;// 权限

    @ApiModelProperty(value = "父权限")
    private Long pid;// 父权限
}