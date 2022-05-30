package com.iotinall.canteen.dto.messdaily;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author WJH
 * @date 2019/11/518:24
 */
@Setter
@Getter
@Accessors(chain = true)
public class ProductEvaluationDetailsDto {

    @ApiModelProperty(value = "菜品ID", name = "id")
    private Long id;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "图片", name = "image")
    private String image;

    @ApiModelProperty(value = "菜品名称", name = "name")
    private String name;

    @ApiModelProperty(value = "菜品说明", name = "info")
    private String info;

    @ApiModelProperty(value = "类别，工艺，口味，口感", name = "detailsInfoDto")
    private ProductEvaluationDetailsInfoDto detailsInfoDto;

    @ApiModelProperty(value = "做法", name = "makeDtos")
    private String practice;

    @ApiModelProperty(value = "原料", name = "material")
    private String material;

    private String catalog;

    @ApiModelProperty(value = "是否已推荐", name = "isRecommend")
    private Boolean isRecommend;

    @ApiModelProperty(value = "推荐数量", name = "recommendCount")
    private Integer recommendCount;

    @ApiModelProperty(value = "是否适宜")
    private Boolean suitable;

    /**
     * 适宜疾病
     */
    @ApiModelProperty(value = "适宜疾病", name = "suitableDisease")
    private String suitableDisease;

    /**
     * 禁忌疾病
     */
    @ApiModelProperty(value = "禁忌疾病", name = "contraindications")
    private String contraindications;

    @ApiModelProperty(value = "平均星星数量", name = "star")
    private Double star;

}
