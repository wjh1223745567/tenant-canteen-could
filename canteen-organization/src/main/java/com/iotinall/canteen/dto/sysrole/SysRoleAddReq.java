package com.iotinall.canteen.dto.sysrole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加角色表请求
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
@Data
@ApiModel(description = "添加角色表请求")
public class SysRoleAddReq {

    @ApiModelProperty(value = "角色名", required = true)
    @NotBlank(message = "请填写角色名")
    private String name;// 角色名

    @NotNull(message = "租户ID不能为空")
    private Long tenantOrgId;

    @ApiModelProperty(value = "备注")
    private String remark;// 备注
}