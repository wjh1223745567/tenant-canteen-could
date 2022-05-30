package com.iotinall.canteen.dto.messprod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加菜品请求
 *
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Data
@ApiModel(description = "添加菜品请求")
public class SysMessProductAddReq {

    @ApiModelProperty(value = "产品名称", required = true)
    @NotBlank(message = "请填写产品名称")
    private String name;// 产品名称

    @NotBlank(message = "请选择用途")
    private String catalog; // 早餐、中餐、晚餐，外卖

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "请选择状态")
    private Boolean enabled;// 是否启用

    @ApiModelProperty(value = "营养介绍")
    @Deprecated
    private String intro;// 营养介绍

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @ApiModelProperty(value = "图片")
    @NotBlank(message = "请上传照片")
    private String img;// 图片

    //刘俊编辑于2020-03-26
    @ApiModelProperty(value = "工艺")
    private String craftId;

    @ApiModelProperty(value = "口味")
    private String flavourId;

    @ApiModelProperty(value = "口感")
    private String taste;

    @ApiModelProperty(value = "类别,多选,多个以逗号隔开")
    private String cuisineIds;

    @ApiModelProperty(value = "制作提示")
    private String practiceTips;

    @ApiModelProperty(value = "健康提示")
    private String healthTips;

    @ApiModelProperty(value = "食物相克")
    private String restriction;

    /**
     * 适宜疾病
     */
    @ApiModelProperty(value = "适宜疾病分号分割")
    private String suitableDisease;

    /**
     * 禁忌疾病
     */
    @ApiModelProperty(value = "禁忌疾病分号分割")
    private String contraindications;

    @ApiModelProperty(value = "用途 1-每日菜谱编排 2-烹饪教程发布 3-全部")
    private String useFor;

    @ApiModelProperty(value = "菜品做法,JSON字符串,格式:[{'description':'清洗干净','fileType':'0','filePath':'1'},{'description':'切菜','fileType':'0','filePath':'1'}]")
    private String practice;

    /**
     * id 原材料ID
     * name 原材料名称
     * amount 数量
     * master 0-主料 1-辅料 2-调料
     * source 来源
     * hasInspectionReport 是否有验收报告 true -有 false -没有
     * acceptor '验收人'
     **/
    @ApiModelProperty(value = "原材料,JSON字符串,格式:[{'id':'000C8C1C408B4DC184A6C4A659FDA077','name':'豆瓣菜','amount':'1','sort':0,'master':0,'source':'AAA','hasInspectionReport':'0','acceptor':'阿香'},{'id':'000C8C1C408B4DC184A6C4A659FDA077','name':'豆瓣菜','amount':'1','sort':0,'master':0,'source':'AAA','hasInspectionReport':'0','acceptor':'阿香'}]")
    private String material;

    @ApiModelProperty(value = "食物相克")
    private String dishId;

    @ApiModelProperty(value = "菜品归类")
    private Integer dishClass;
}

