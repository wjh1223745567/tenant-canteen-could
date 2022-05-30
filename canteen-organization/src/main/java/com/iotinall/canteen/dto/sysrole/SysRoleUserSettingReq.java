package com.iotinall.canteen.dto.sysrole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xin-bing
 * @date 10/27/2019 16:17
 */
@Data
@ApiModel(description = "角色用户设置")
public class SysRoleUserSettingReq {
    @ApiModelProperty(value = "用户id")
    private List<Long> empIds;
}
