package com.iotinall.canteen.dto.dish;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.dish.NutritionDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 原材料DTO
 *
 * @author loki
 * @date 2020/03/25 11:10
 */
@Getter
@Setter
public class SysMaterialDTO extends NutritionDTO {
    private String id;

    private String name;

    /**
     * 菜品图片路径
     */
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    /**
     * 原料类型
     */
    private String materialTypeId;

    /**
     * 原料类型名称
     */
    private String materialTypeName;

    /**
     * 别名
     */
    private String alias;

    /**
     * 用于后台检索
     */
    private String search;

    /**
     * 原料使用提示
     */
    private String tips;
    /**
     * 原料介绍
     */
    private String introduce;
    /**
     * 原料营养分析
     */
    private String nutritionAnalysis;
    /**
     * 原料适用人群
     */
    private String forPeople;
    /**
     * 相克原料
     */
    private String restriction;
    /**
     * 原料制作指导
     */
    private String guidance;
    /**
     * 原料食疗作用
     */
    private String dietaryTherapy;

    /**
     * 备注
     */
    private String description;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
