package com.iotinall.canteen.dto.role;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加角色DTO
 *
 * @author loki
 * @date 2021/7/16 15:36
 **/
@Data
public class FeignRoleAddReq implements Serializable {
    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "租户组织ID不能为空")
    private Long tenantOrgId;
}
