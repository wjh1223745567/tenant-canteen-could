package com.iotinall.canteen.dto.operationrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改kitchen_operation_record
 * @author xinbing
 * @date 2020-07-09 15:26:01
 */
@Data
@ApiModel(description = "修改kitchen_operation_record")
public class KitchenOperationRecordEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "记录照片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    @ApiModelProperty(value = "餐次类型", required = true)
    @NotNull(message = "请填写餐次类型")
    private Integer mealType; // 餐次类型

    @ApiModelProperty(value = "检查结果")
    private Integer state; // 检查结果

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "清洗类型")
    private Long itemId; // 清洗类型

    @ApiModelProperty(value = "责任人id", required = true)
    @NotNull(message = "请填写责任人id")
    private Long dutyEmpId; // 责任人id
}