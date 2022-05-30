package com.iotinall.canteen.dto.safetyinspectrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 *
 * @author xinbing
 * @date 2020-07-10 11:10:12
 */
@Data
@ApiModel(description = "消防安全DTO")
public class KitchenSafetyInspectRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "检查时间", required = true)
    private LocalDateTime recordTime; // 时间

    @ApiModelProperty(value = "图片", required = true)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "状态", required = true)
    private Integer state; // 状态

    @ApiModelProperty(value = "检查结果")
    private String comments; // 检查结果

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "检查项id")
    private Long itemId; // 检查项id

    @ApiModelProperty(value = "检查项名")
    private String itemName;

    @ApiModelProperty(value = "检查项描述")
    private String itemDesc;

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