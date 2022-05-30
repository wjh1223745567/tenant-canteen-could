package com.iotinall.canteen.dto.tenantuser;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TenantUserResp {

    private Long id;

    private String name;

    /**
     * 租户CODE
     */
    private String code;

    /**
     * 数据库连接 IP：端口
     */
    private String sqlUrl;

    private String sqlUsername;

}
