package com.iotinall.canteen.dto.garbage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 添加餐厨垃圾
 * @author xinbing
 * @date 2020-07-10 11:46:09
 */
@Data
@ApiModel(description = "添加餐厨垃圾")
public class KitchenGarbageRecordAddReq {

    @ApiModelProperty(value = "照片", required = true)
    //@NotBlank(message = "照片不能为空")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 照片

    @ApiModelProperty(value = "检查时间", required = true)
    @NotNull(message = "检查时间不能为空")
    private LocalDateTime recordTime; // 检查时间

    @ApiModelProperty(value = "餐次", required = true)
    @NotNull(message = "餐次不能为空")
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "检查状态", required = true)
    @NotNull(message = "检查状态不能为空")
    private Integer state; // 检查状态

    @ApiModelProperty(value = "检查评论")
    private String comments; // 检查评论

    @ApiModelProperty(value = "责任人id", required = true)
    @NotNull(message = "责任人id不能为空")
    private Long dutyEmpId; // 责任人id
}