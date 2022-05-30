package com.iotinall.canteen.dto.dish;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加菜谱请求参数
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@ApiModel(description = "添加菜谱请求参数")
@Getter
@Setter
public class SysMaterialAddReq extends NutritionDTO implements Serializable {
    @ApiModelProperty(value = "原料图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @NotBlank(message = "原料图片不能为空")
    private String img;

    @ApiModelProperty(value = "原料名称")
    @NotBlank(message = "原料名称不能为空")
    private String name;

    @ApiModelProperty(value = "原料类型")
    @NotBlank(message = "原料类型不能为空")
    private String materialTypeId;

    @ApiModelProperty(value = "原料介绍")
    private String introduce;

    @ApiModelProperty(value = "适用人群")
    private String forPeople;

    @ApiModelProperty(value = "食疗作用")
    private String dietaryTherapy;

    @ApiModelProperty(value = "原料别名")
    private String alias;

    @ApiModelProperty(value = "使用提示")
    private String tips;

    @ApiModelProperty(value = "相克原料")
    private String restriction;

    @ApiModelProperty(value = "营养分析")
    private String nutritionAnalysis;

    @ApiModelProperty(value = "制作指导")
    private String guidance;
}
