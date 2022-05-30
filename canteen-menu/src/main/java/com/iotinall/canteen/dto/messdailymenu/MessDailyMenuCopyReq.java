package com.iotinall.canteen.dto.messdailymenu;

import com.iotinall.canteen.constants.MealTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(value = "复制菜谱请求参数")
@Data
public class MessDailyMenuCopyReq {

    @ApiModelProperty(value = "餐次")
    @NotNull(message = "请选择餐次")
    private MealTypeEnum code;

    @ApiModelProperty(value = "时间")
    @NotNull(message = "请填写menu_date")
    private LocalDate menuDate;

    @ApiModelProperty(value = "复制时间")
    @NotNull(message = "请填写copy_date")
    private LocalDate copyDate;

}
