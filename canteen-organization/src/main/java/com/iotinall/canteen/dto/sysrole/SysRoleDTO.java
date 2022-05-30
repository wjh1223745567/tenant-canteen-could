package com.iotinall.canteen.dto.sysrole;

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
@ApiModel(description = "返回角色表结果")
public class SysRoleDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;// id

    @ApiModelProperty(value = "角色名", required = true)
    private String name;// 角色名

    @ApiModelProperty(value = "租户ID")
    private Long tenantOrgId;

    @ApiModelProperty(value = "备注")
    private String remark;// 备注
}