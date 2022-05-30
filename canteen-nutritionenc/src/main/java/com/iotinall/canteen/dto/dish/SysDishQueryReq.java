package com.iotinall.canteen.dto.dish;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统菜谱列表查询条件
 *
 * @author loki
 * @date 2020/08/22 10:36
 */
@ApiModel(value = "食谱百科列表请求参数")
@Data
public class SysDishQueryReq {
    @ApiModelProperty(value = "类别ID")
    private String cuisineId;

    @ApiModelProperty(value = "工艺ID")
    private String craftId;

    @ApiModelProperty(value = "口味ID")
    private String flavoursId;

    @ApiModelProperty(value = "食用  1-早餐 2-午餐 3-晚餐 ，多个传数组")
    private Integer catalog;

    @ApiModelProperty(value = "名称")
    private String dishName;
}
