package com.iotinall.canteen.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Data
@ApiModel(description = "组织组织")
public class OrgDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;// id

    @ApiModelProperty(value = "组织名称", required = true)
    private String name;// 组织名称

    @ApiModelProperty(value = "员工数量", required = true)
    private Integer empCount;// 员工数量

    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;// 排序

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;// 创建时间

    @ApiModelProperty(value = "父级组织")
    private Long parentId;// 父级组织
}