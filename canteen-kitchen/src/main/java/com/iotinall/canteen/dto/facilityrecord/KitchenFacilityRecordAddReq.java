package com.iotinall.canteen.dto.facilityrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 添加kitchen_facility_record
 * @author xinbing
 * @date 2020-07-10 11:32:53
 */
@Data
@ApiModel(description = "添加kitchen_facility_record")
public class KitchenFacilityRecordAddReq {

    @ApiModelProperty(value = "图片", required = true)
    //@NotBlank(message = "图片不能为空")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "检查时间", required = true)
    @NotNull(message = "检查时间不能为空")
    private LocalDateTime recordTime; // 检查时间

    @ApiModelProperty(value = "餐次", required = true)
    @NotNull(message = "餐次不能为空")
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "检查状态", required = true)
    @NotNull(message = "检查状态不能为空")
    private Integer state; // 检查状态

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "类型id", required = true)
    @NotNull(message = "类型id不能为空")
    private Long itemId; // 类型id

    @ApiModelProperty(value = "责任人id", required = true)
    @NotNull(message = "责任人id不能为空")
    private Long dutyEmpId; // 责任人id
}