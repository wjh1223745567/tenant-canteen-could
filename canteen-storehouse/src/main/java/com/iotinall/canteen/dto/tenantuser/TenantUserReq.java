package com.iotinall.canteen.dto.tenantuser;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class TenantUserReq {

    private Long id;

    /**
     * 名称
     */
    @Length(max = 80)
    @NotBlank
    private String name;

    /**
     * 租户CODE
     */
    @Length(max = 20)
    @NotBlank
    private String code;

    /**
     * 数据库连接 IP：端口
     */
    @NotBlank
    @Length(max = 255)
    private String sqlUrl;

    @NotBlank
    @Length(max = 255)
    private String sqlUsername;

    @NotBlank
    @Length(max = 255)
    private String sqlPassword;

}
