package com.iotinall.canteen.dto.messdailymenu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
* @author xin-bing
* @date 2019-10-22 16:10:53
*/
@Data
@ApiModel(description = "查询mess_daily_menu条件")
public class MessDailyMenuQueryCriteria{
    @ApiModelProperty(value = "菜谱日期")
    @NotNull(message = "菜谱日期不能为空")
    private LocalDate menuDate;
}