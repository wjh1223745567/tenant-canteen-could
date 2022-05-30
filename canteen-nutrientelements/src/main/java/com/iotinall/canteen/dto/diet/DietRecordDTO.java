package com.iotinall.canteen.dto.diet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 饮食记录
 *
 * @author loki
 * @date 2020/04/10 19:38
 */
@Data
public class DietRecordDTO implements Serializable {
    /**
     * 食物ID
     */
    private Long dishId;
    /**
     * 食物名称
     */
    private String dishName;
    /**
     * 食物重量 单位g
     */
    private BigDecimal dishWeight;
    /**
     * 食物单位能量
     */
    private BigDecimal unitEnergy;

    /**
     * 食物总能量
     */
    private BigDecimal totalEnergy;

    /**
     * 食物图片
     */
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    /**
     * 菜品类型 0-系统中菜品 1-自定义菜品
     */
    private Integer dishType;


    /**
     * 0-早餐 1-午餐 2-晚餐 3-加餐
     */
    @ApiModelProperty(value = "饮食类型 0-早餐 1-午餐 2-晚餐 3-加餐", required = true)
    private Integer type;
}
