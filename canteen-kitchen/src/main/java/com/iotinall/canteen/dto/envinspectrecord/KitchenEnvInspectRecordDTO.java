package com.iotinall.canteen.dto.envinspectrecord;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.item.ItemDutyEmpDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author xinbing
 * @date 2020-07-10 13:48:40
 */
@Data
@ApiModel(description = "环境卫生DTO")
public class KitchenEnvInspectRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "图片", required = true)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "检查时间", required = true)
    private LocalDateTime recordTime;

    @ApiModelProperty(value = "餐次类型", required = true)
    private Integer mealType; // 餐次类型

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "检查项id", required = true)
    private Long itemId; // 检查项id

    @ApiModelProperty(value = "检查项名称")
    private String itemName; // 检查项名称

    @ApiModelProperty(value = "检查项描述")
    private String itemDesc;

    @ApiModelProperty(value = "责任人")
    private List<ItemDutyEmpDTO> envDutyEmpDTOList;

    @ApiModelProperty(value = "检查人id", required = true)
    private Long auditorId; // 检查人id

    @ApiModelProperty(value = "检查人姓名")
    private String auditorName;

    @ApiModelProperty(value = "检查人职位")
    private String role;
}