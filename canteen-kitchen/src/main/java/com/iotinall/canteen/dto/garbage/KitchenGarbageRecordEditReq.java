package com.iotinall.canteen.dto.garbage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 修改餐厨垃圾
 * @author xinbing
 * @date 2020-07-10 11:46:09
 */
@Data
@ApiModel(description = "修改餐厨垃圾")
public class KitchenGarbageRecordEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "照片", required = true)
    //@NotBlank(message = "请填写照片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 照片

    @ApiModelProperty(value = "检查时间", required = true)
    @NotNull(message = "请填写检查时间")
    private LocalDateTime recordTime; // 检查时间

    @ApiModelProperty(value = "餐次", required = true)
    @NotNull(message = "请填写餐次")
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "检查状态", required = true)
    @NotNull(message = "请填写检查状态")
    private Integer state; // 检查状态

    @ApiModelProperty(value = "检查评论")
    private String comments; // 检查评论

    @ApiModelProperty(value = "责任人id", required = true)
    @NotNull(message = "请填写责任人id")
    private Long dutyEmpId; // 责任人id
}