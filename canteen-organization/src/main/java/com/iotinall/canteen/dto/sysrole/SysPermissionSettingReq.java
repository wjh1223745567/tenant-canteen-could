package com.iotinall.canteen.dto.sysrole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 权限设置请求
 *
 * @author xin-bing
 * @date 10/26/2019 20:25
 */
@Data
@ApiModel(description = "权限设置")
public class SysPermissionSettingReq {
    @ApiModelProperty(value = "权限类型")
    private Integer type;

    @ApiModelProperty(value = "权限id")
    private List<String> permissions;
}
