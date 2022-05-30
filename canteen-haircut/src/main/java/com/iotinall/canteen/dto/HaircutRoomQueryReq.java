package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:理发室查询条件
 * @author: JoeLau
 * @time: 2021年06月23日 18:17:42
 */
@Data
public class HaircutRoomQueryReq {

    @ApiModelProperty(value = "关键字")
    private String keywords;

    /**
     * 手机当前经度
     */
    @ApiModelProperty(value = "手机当前经度")
    private BigDecimal nowLongitude;

    /**
     * 手机当前纬度
     */
    @ApiModelProperty(value = "手机当前纬度")
    private BigDecimal nowLatitude;
}
