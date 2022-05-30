package com.iotinall.canteen.dto.messprod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author xin-bing
 * @date 10/29/2019 10:58
 */
@Data
@ApiModel(description = "菜品详情")
public class SysMessProductDetailDTO implements Serializable {
    @ApiModelProperty(value = "id", required = true)
    private Long id;// id

    @ApiModelProperty(value = "产品名称", required = true)
    private String name;// 产品名称

    @ApiModelProperty(value = "早餐、中餐、完餐", required = true)
    private String catalog;// 早餐、中餐、完餐

    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean enabled;// 是否启用

    @ApiModelProperty(value = "营养介绍")
    private String intro;// 营养介绍

    @ApiModelProperty(value = "图片")

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;// 图片

    @ApiModelProperty(value = "推荐数", required = true)
    private Integer recommendedCount;// 推荐数

    @ApiModelProperty(value = "销售数量", required = true)
    private Integer soldCount;// 销售数量

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;// 创建时间

    @ApiModelProperty(value = "remark")
    private String remark;// remark

    @ApiModelProperty(value = "评分数量，是一个数组，5星wei")
    private Map<Integer, Integer> starCounts; // 评分数量

    @ApiModelProperty(value = "教程")
    private String practice;

    @ApiModelProperty(value = "菜品工艺ID")
    private String craftId;

    @ApiModelProperty(value = "菜品工艺名称")
    private String craftName;

    @ApiModelProperty(value = "菜品口味ID")
    private String flavourId;

    @ApiModelProperty(value = "菜品口味名称")
    private String flavourName;

    @ApiModelProperty(value = "口感")
    private String taste;

    @ApiModelProperty(value = "类别,多选")
    private List<SysCuisineDTO> cuisines;

    /**
     * 适宜疾病
     */
    @ApiModelProperty(value = "适宜疾病")
    private String suitableDisease;

    /**
     * 禁忌疾病
     */
    @ApiModelProperty(value = "禁忌疾病")
    private String contraindications;
    /**
     * 适宜疾病
     */
    @ApiModelProperty(value = "适宜疾病")
    private String suitableDiseaseName;

    /**
     * 禁忌疾病
     */
    @ApiModelProperty(value = "禁忌疾病")
    private String contraindicationsName;

    @ApiModelProperty(value = "制作提示")
    private String practiceTips;

    @ApiModelProperty(value = "健康提示")
    private String healthTips;

    @ApiModelProperty(value = "食物相克")
    private String restriction;

    @ApiModelProperty(value = "用途 1-每日菜谱编排 2-烹饪教程发布 3-全部")
    private String useFor;

    @ApiModelProperty(value = "菜品做法")
    private List<MessProductPracticeDTO> practices;

    @ApiModelProperty(value = "材料")
    private List<MessProductMaterialDTO> materials;// 材料

    @ApiModelProperty(value = "菜品归类")
    private Integer dishClass;
}
