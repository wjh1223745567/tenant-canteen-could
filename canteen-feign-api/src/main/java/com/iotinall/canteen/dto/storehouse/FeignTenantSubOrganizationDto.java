package com.iotinall.canteen.dto.storehouse;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class FeignTenantSubOrganizationDto implements Serializable {

    private Long id;

    private String name;

    /**
     * 关联食堂组织ID,不可以修改
     */
    private Long tenantOrgId;

    /**
     * 容纳人数
     */
    private Integer capacity;

    private String address;

    /**
     * 面向人群
     */
    private String forTheCrowd;

    /**
     * 联系人
     */
    private String contactPerson;

    private String phone;
}
