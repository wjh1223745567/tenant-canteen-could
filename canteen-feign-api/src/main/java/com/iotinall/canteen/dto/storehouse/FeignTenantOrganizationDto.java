package com.iotinall.canteen.dto.storehouse;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FeignTenantOrganizationDto {

    private Long id;

    private String name;

    /**
     * 类型，后厨，库存，餐厅。
     * TenantOrganizationTypeEnum
     */
    private Integer type;

    /**
     * 对应数据库KEY TenantUser 表code 字段
     */
    private String dataSourceKey;

    /**
     * 父级节点
     */
    private Long pid;

}
