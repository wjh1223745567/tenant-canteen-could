package com.iotinall.canteen.dto.messprod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Data
@ApiModel(description = "返回菜品结果")
public class SysMessProductDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "产品名称", required = true)
    private String name;

    private String catalog;

    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean enabled;

    @ApiModelProperty(value = "材料")
    private List<MessProductMaterialDTO> materials;

    @ApiModelProperty(value = "营养介绍")
    private String intro;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "推荐数", required = true)
    private Integer recommendedCount;

    @ApiModelProperty(value = "销售数量", required = true)
    private Integer soldCount;

    @ApiModelProperty(value = "好评率")
    private BigDecimal favorRate;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "菜品工艺id")
    private String craftId;

    @ApiModelProperty(value = "菜品工艺名称")
    private String craftName;

    @ApiModelProperty(value = "菜品口味id")
    private String flavourId;

    @ApiModelProperty(value = "菜品口味名称")
    private String flavourName;

    @ApiModelProperty(value = "口感")
    private String taste;

    @ApiModelProperty(value = "类别,多选")
    private List<SysCuisineDTO> cuisines;

    @ApiModelProperty(value = "制作提示")
    private String practiceTips;

    @ApiModelProperty(value = "健康提示")
    private String healthTips;

    @ApiModelProperty(value = "食物相克")
    private String restriction;

    /**
     * 适宜疾病
     */
    @ApiModelProperty(value = "适宜疾病")
    private String suitableDisease;

    @ApiModelProperty(value = "适宜疾病")
    private String suitableDiseaseName;

    /**
     * 禁忌疾病
     */
    @ApiModelProperty(value = "禁忌疾病")
    private String contraindications;

    @ApiModelProperty(value = "禁忌疾病")
    private String contraindicationsName;

    @ApiModelProperty(value = "用途 1-每日菜谱编排 2-烹饪教程发布 3-全部")
    private String useFor;

    @ApiModelProperty(value = "菜品做法")
    private List<MessProductPracticeDTO> practices;

    @ApiModelProperty(value = "菜品归类")
    private Integer dishClass;
}