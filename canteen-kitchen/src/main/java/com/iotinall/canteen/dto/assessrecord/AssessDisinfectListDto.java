package com.iotinall.canteen.dto.assessrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.disinfect.KitchenDisinfectDutyEmpDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "考核记录---消毒管理")
public class AssessDisinfectListDto implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id; // 主键

    @ApiModelProperty(value = "消毒时间")
    private LocalDateTime recordTime; // 消毒时间

    @ApiModelProperty(value = "照片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img; // 照片

    @ApiModelProperty(value = "消毒项id")
    private Long itemId; // 消毒项

    @ApiModelProperty(value = "消毒项名")
    private String itemName;

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "责任人")
    private List<KitchenDisinfectDutyEmpDTO> kitchenDisinfectDutyEmpDTOS;

    @ApiModelProperty(value = "检查备注")
    private String comments;

    @ApiModelProperty(value = "消毒要求")
    private String requirements;
}

