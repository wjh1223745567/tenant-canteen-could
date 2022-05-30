package com.iotinall.canteen.dto.messdailymenu;

import com.iotinall.canteen.constants.MealTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 添加mess_daily_menu请求
 *
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Data
@ApiModel(description = "添加mess_daily_menu请求")
public class MessDailyMenuAddReq {

    @ApiModelProperty(value = "菜谱日期，格式yyyy-MM-dd", required = true)
    @NotNull(message = "请填写menu_date")
    private LocalDate menuDate;

    @ApiModelProperty(value = "用途", required = true, allowableValues = "0, 1, 2")
    @NotNull(message = "请填写meal_type")
    private MealTypeEnum mealType;

    @ApiModelProperty(value = "remark")
    private String remark;

    @ApiModelProperty(value = "菜品id")
    @NotNull(message = "请选择菜品")
    private Long productId;
}