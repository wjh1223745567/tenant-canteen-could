package com.iotinall.canteen.dto.sample;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改留样管理
 * @author xinbing
 * @date 2020-07-06 17:09:03
 */
@Data
@ApiModel(description = "修改留样管理")
public class KitchenSampleEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键


    @ApiModelProperty(value = "检查图片，用逗号分隔")
    private String img;

//    @ApiModelProperty(value = "检查时间", required = true)
//    @NotNull(message = "检查时间不能为空")
//    private LocalDateTime recordTime; // 检查时间

    @ApiModelProperty(value = "餐次", required = true)
    @NotNull(message = "餐次不能为空")
    private Integer mealType; // 餐次

//    @ApiModelProperty(value = "责任人ID")
//    private List<Long> empIds;

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime; // 创建时间

}