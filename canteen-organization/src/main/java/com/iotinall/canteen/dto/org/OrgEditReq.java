package com.iotinall.canteen.dto.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加组织请求
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Data
@ApiModel(description = "修改组织请求")
public class OrgEditReq {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;// id

    @ApiModelProperty(value = "组织名称", required = true)
    @NotBlank(message = "请填写组织名称")
    private String name;// 组织名称

    @ApiModelProperty(value = "父级组织id")
    private Long parentId;// 父级组织

    private Long tenantId;
}