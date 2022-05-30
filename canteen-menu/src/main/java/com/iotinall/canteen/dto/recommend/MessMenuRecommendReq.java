package com.iotinall.canteen.dto.recommend;

import com.iotinall.canteen.constants.MealTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 添加评论
 * @author WJH
 * @date 2019/11/610:54
 */
@Setter
@Getter
@ApiModel(description = "推荐请求")
public class MessMenuRecommendReq {

    @ApiModelProperty(value = "菜品ID", name = "productId", required = true)
    @NotNull(message = "菜品ID不能为空")
    private Long productId;

    @ApiModelProperty(value = "日期", name = "date", required = true)
    @NotNull
    private String date;

    @ApiModelProperty(value = "饮食类型", name = "mealType")
    @NotNull
    private MealTypeEnum mealType;

    @ApiModelProperty(value = "是否推荐", name = "recommend")
    @NotNull
    private Boolean recommend;

}
