package com.iotinall.canteen.dto.facilityrecord;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.facilityduty.FacilityDutyEmpDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author xinbing
 * @date 2020-07-10 11:32:53
 */
@Data
@ApiModel(description = "kitchen_facility_recordDTO")
public class KitchenFacilityRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "图片", required = true)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "检查时间", required = true)
    private LocalDateTime recordTime; // 检查时间

    @ApiModelProperty(value = "餐次", required = true)
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "状态", required = true)
    private Integer state; // 状态

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "设施类型", required = true)
    private Long itemId; // 设施类型

    @ApiModelProperty(value = "item名称")
    private String itemName; // item名称

    @ApiModelProperty(value = "描述")
    private String itemDesc;

    @ApiModelProperty(value = "责任人", required = true)
    private List<FacilityDutyEmpDTO> facilityDutyEmpDTOList;

    private String role;

    @ApiModelProperty(value = "检查人id", required = true)
    private Long auditorId; // 检查人id

    @ApiModelProperty(value = "检查人名称")
    private String auditorName;
}