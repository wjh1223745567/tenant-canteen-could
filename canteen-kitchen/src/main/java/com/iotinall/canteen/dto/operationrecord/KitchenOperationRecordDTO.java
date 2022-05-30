package com.iotinall.canteen.dto.operationrecord;

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
 * @date 2020-07-09 15:26:01
 */
@Data
@ApiModel(description = "kitchen_operation_recordDTO")
public class KitchenOperationRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img;

    @ApiModelProperty(value = "餐次", required = true)
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "检查结果")
    private Integer state; // 检查结果

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "操作时间", required = true)
    private LocalDateTime recordTime; // 操作时间

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "类型")
    private Long itemId; // 类型

    @ApiModelProperty(value = "类型名称")
    private String itemName;

    @ApiModelProperty(value = "类型描述")
    private String itemDesc;

    @ApiModelProperty(value = "责任人id", required = true)
    private Long dutyEmpId; // 责任人id

    @ApiModelProperty(value = "责任人姓名")
    private String dutyEmpName;

    @ApiModelProperty(value = "责任人头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String dutyEmpAvatar;

    @ApiModelProperty(value = "审核人id", required = true)
    private Long auditorId; // 审核人id

    @ApiModelProperty(value = "审核人姓名")
    private String auditorName;

    private String role;
}