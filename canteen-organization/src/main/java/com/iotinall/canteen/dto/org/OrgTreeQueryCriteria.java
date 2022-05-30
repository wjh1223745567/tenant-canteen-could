package com.iotinall.canteen.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 树搜索条件
 * @author WJH
 * @date 2019/10/2816:36
 */
@Data
@ApiModel(description = "树搜索条件")
public class OrgTreeQueryCriteria {

    @ApiModelProperty(value = "name")
    private String name;

}
