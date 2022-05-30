package com.iotinall.canteen.dto.foodnotetype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @description: 美食笔记类型编辑请求参数Req
 * @author: JoeLau
 * @time: 2021年07月06日 14:22:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "美食笔记类型编辑请求参数")
public class FoodNoteTypeEditReq extends FoodNoteTypeAddReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

}
