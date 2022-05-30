package com.iotinall.canteen.dto.tenantorg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TenantOrganizationTreeDto {

    private Long id;

    private String name;

    private String type;

    private String dataSourceKey;

    private Long pid;

    private List<TenantOrganizationTreeDto> children;

}
