package com.iotinall.canteen.dto.tenantuser;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TenantUserSelect {

    private Long id;

    private String name;

    private String code;

}
