package com.iotinall.canteen.dto.cookrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 修改kitchen_operation_record
 * @author xinbing
 * @date 2020-07-09 15:26:01
 */
@Data
@ApiModel(description = "编辑烹饪记录")
public class CookRecordEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "记录照片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    @ApiModelProperty(value = "菜品id")
    private Long productId;

    @ApiModelProperty(value = "操作时间", required = true)
    @NotNull(message = "操作时间不能为空")
    private LocalDateTime recordTime;

    @ApiModelProperty(value = "餐次类型", required = true)
    @NotNull(message = "餐次类型不能为空")
    private Integer mealType; // 餐次类型

    @ApiModelProperty(value = "检查结果")
    private Integer state; // 检查结果

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "责任人id", required = true)
    @NotNull(message = "责任人id不能为空")
    private Long dutyEmpId; // 责任人id
}