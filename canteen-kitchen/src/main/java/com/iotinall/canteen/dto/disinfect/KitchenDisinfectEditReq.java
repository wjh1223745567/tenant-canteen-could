package com.iotinall.canteen.dto.disinfect;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 修改消毒管理
 * @author xinbing
 * @date 2020-07-06 15:32:49
 */
@Data
@ApiModel(description = "修改消毒管理")
public class KitchenDisinfectEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "消毒时间", required = true)
    @NotNull(message = "请填写消毒时间")
    private LocalDateTime recordTime; // 消毒时间

    @ApiModelProperty(value = "照片", required = true)
    //@NotBlank(message = "请填写照片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 照片

    @ApiModelProperty(value = "消毒项")
    private Long itemId; // 消毒项

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "责任人", required = true)
    @NotNull(message = "请填写责任人")
    private Long dutyEmpId; // 责任人

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "消毒要求")
    private String requirements;
}