package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @description:编辑理发室请求参数
 * @author: JoeLau
 * @time: 2021年06月23日 17:52:37
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "编辑理发室请求参数")
public class HaircutRoomEditReq extends HaircutRoomAddReq{

    @ApiModelProperty(value = "理发室id")
    @NotNull(message = "理发室不能为空")
    private Long id;

}
