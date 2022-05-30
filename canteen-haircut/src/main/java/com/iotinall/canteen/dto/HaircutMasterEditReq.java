package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @description:编辑理发师
 * @author: JoeLau
 * @time: 2021年06月24日 13:53:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "编辑理发师请求参数")
public class HaircutMasterEditReq extends HaircutMasterAddReq {

    @ApiModelProperty(value = "理发师id")
    @NotNull(message = "理发师不能为空")
    private Long id;

}
