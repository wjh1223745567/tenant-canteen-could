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
 * 修改添加剂记录
 * @author xinbing
 * @date 2020-07-10 14:24:45
 */
@Data
@ApiModel(description = "修改添加剂记录")
public class KitchenFoodAdditiveRecordEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "图片", required = true)
    //@NotBlank(message = "请填写图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "添加时间", required = true)
    @NotNull(message = "请填写添加时间")
    private LocalDateTime recordTime; // 添加时间

    @ApiModelProperty(value = "检测状态", required = true)
    @NotNull(message = "请填写检测状态")
    private Integer state; // 检测状态

    @ApiModelProperty(value = "检查评论")
    private String comments; // 检查评论

    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "请填写产品id")
    private Long productId; // 产品id

    @ApiModelProperty(value = "添加剂id", required = true)
    @NotNull(message = "请填写添加剂id")
    private Long itemId; // 添加剂id

    @ApiModelProperty(value = "剂量")
    @NotNull(message = "剂量不能为空")
    private BigDecimal dose;

    @ApiModelProperty(value = "责任人id", required = true)
    @NotNull(message = "请填写责任人id")
    private Long dutyEmpId; // 责任人id
}