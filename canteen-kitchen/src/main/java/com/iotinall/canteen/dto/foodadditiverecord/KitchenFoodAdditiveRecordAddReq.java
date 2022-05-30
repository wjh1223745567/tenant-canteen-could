package com.iotinall.canteen.dto.foodadditiverecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 添加添加剂记录
 * @author xinbing
 * @date 2020-07-10 14:24:45
 */
@Data
@ApiModel(description = "添加添加剂记录")
public class KitchenFoodAdditiveRecordAddReq {

    @ApiModelProperty(value = "图片", required = true)
    //@NotBlank(message = "图片不能为空")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "添加时间", required = true)
    @NotNull(message = "添加时间不能为空")
    private LocalDateTime recordTime; // 添加时间

    @ApiModelProperty(value = "检测状态", required = true)
    @NotNull(message = "检测状态不能为空")
    private Integer state; // 检测状态

    @ApiModelProperty(value = "检查评论")
    private String comments; // 检查评论

    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long productId; // 产品id

    @ApiModelProperty(value = "添加剂id", required = true)
    @NotNull(message = "添加剂id不能为空")
    private Long itemId; // 添加剂id

    @ApiModelProperty(value = "剂量")
    @NotNull(message = "剂量不能为空")
    private BigDecimal dose;

    @ApiModelProperty(value = "责任人id", required = true)
    @NotNull(message = "责任人id不能为空")
    private Long dutyEmpId; // 责任人id
}