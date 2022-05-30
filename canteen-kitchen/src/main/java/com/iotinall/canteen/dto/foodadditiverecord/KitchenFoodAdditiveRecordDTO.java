package com.iotinall.canteen.dto.foodadditiverecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 *
 * @author xinbing
 * @date 2020-07-10 14:24:45
 */
@Data
@ApiModel(description = "添加剂记录DTO")
public class KitchenFoodAdditiveRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "图片", required = true)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "添加时间", required = true)
    private LocalDateTime recordTime; // 添加时间

    @ApiModelProperty(value = "状态", required = true)
    private Integer state; // 状态

    @ApiModelProperty(value = "检测评价")
    private String comments; // 检测评价

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "产品id", required = true)
    private Long productId; // 产品id

    @ApiModelProperty(value = "产品名称")
    private String productName; // 产品名称

    @ApiModelProperty(value = "添加剂id", required = true)
    private Long itemId; // 添加剂id

    @ApiModelProperty(value = "添加剂名称")
    private String itemName;

    @ApiModelProperty(value = "描述")
    private String itemDesc;

    @ApiModelProperty(value = "剂量")
    private BigDecimal dose;

    @ApiModelProperty(value = "责任人id", required = true)
    private Long dutyEmpId; // 责任人id

    @ApiModelProperty(value = "责任人名称")
    private String dutyEmpName;

    @ApiModelProperty(value = "责任人头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String dutyEmpAvatar;

    @ApiModelProperty(value = "检查人id", required = true)
    private Long auditorId; // 检查人id

    @ApiModelProperty(value = "检查人名称")
    private String auditorName;

    private String role;
}