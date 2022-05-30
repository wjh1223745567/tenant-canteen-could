package com.iotinall.canteen.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
@ApiModel(description = "组织接口树")
public class OrgSelectTreeDTO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "name")
    private String name;

    private Boolean isOrg;

    private Boolean haveChildren;
}
