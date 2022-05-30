package com.iotinall.canteen.dto.tenantorg;

import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TenantOrganizationReq {

    private Long id;

    @Length(max = 30)
    @NotBlank
    private String name;

    /**
     * 类型，后厨，库存，餐厅。
     * @see TenantOrganizationTypeEnum
     */
    @NotNull
    private Integer type;

    /**
     * 对应数据库KEY TenantUser 表code 字段
     */
    @Length(max = 50)
    @NotBlank
    private String dataSourceKey;

    /**
     * 父级节点
     */
    private Long pid;

}
