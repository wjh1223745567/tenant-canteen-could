package com.iotinall.canteen.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author WJH
 * @date 2019/10/2816:18
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "组织接口树")
public class OrgTreeDTO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "children")
    private List<OrgTreeDTO> children;

    private Long parentId;

    private Long tenantId;
}
