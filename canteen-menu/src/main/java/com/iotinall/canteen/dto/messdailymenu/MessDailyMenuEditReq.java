package com.iotinall.canteen.dto.messdailymenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 添加mess_daily_menu请求
 * @author xin-bing
 * @date 2019-10-22 16:10:53
 */
@Data
@ApiModel(description = "修改mess_daily_menu请求")
public class MessDailyMenuEditReq {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;// id

    @ApiModelProperty(value = "menu_date", required = true)
    @NotNull(message = "请填写menu_date")
    private LocalDate menuDate;// menu_date

    @ApiModelProperty(value = "meal_type", required = true)
    @NotNull(message = "请填写meal_type")
    private Integer mealType;// meal_type

    @ApiModelProperty(value = "remark")
    private String remark;// remark

    @ApiModelProperty(value = "product_id")
    private Long productId;// product_id
}