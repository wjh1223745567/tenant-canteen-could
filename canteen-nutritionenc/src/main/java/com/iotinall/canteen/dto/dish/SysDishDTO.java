package com.iotinall.canteen.dto.dish;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@Data
@ApiModel(description = "菜品")
public class SysDishDTO implements Serializable {
    private String id;

    @ApiModelProperty(value = "菜品名称")
    private String name;

    @ApiModelProperty(value = "菜谱图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    @ApiModelProperty(value = "营养信息")
    private String intro;

    @ApiModelProperty(value = "口感")
    private String taste;

    @ApiModelProperty(value = "工艺ID")
    private String craftId;

    @ApiModelProperty(value = "工艺名称")
    private String craftName;

    @ApiModelProperty(value = "口味ID")
    private String flavoursId;

    @ApiModelProperty(value = "口味名称")
    private String flavourName;

    @ApiModelProperty(value = "类别")
    private List<SysCuisineDTO> cuisines;

    @ApiModelProperty(value = "菜品原料")
    private List<SysDishMaterialDTO> materials;

    @ApiModelProperty(value = "菜品的做法")
    private String practice;

    @ApiModelProperty(value = "菜品的制作提示")
    private String practiceTips;

    @ApiModelProperty(value = "菜品的健康提示")
    private String healthTips;

    @ApiModelProperty(value = "食物相克，逗号拼接")
    private String restriction;

    /**
     * true - 适合
     * false - 不适合
     */
    @ApiModelProperty(value = "是否适合晚餐")
    private Boolean dinner;

    /**
     * true - 适合
     * false - 不适合
     */
    @ApiModelProperty(value = "是否适合午餐")
    private Boolean lunch;

    /**
     * true - 适合
     * false - 不适合
     */
    @ApiModelProperty(value = "是否适合早餐")
    private Boolean breakfast;
}
