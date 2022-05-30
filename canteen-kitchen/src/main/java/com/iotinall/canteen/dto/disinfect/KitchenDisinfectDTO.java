package com.iotinall.canteen.dto.disinfect;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author xinbing
 * @date 2020-07-06 15:32:49
 */
@Data
@ApiModel(description = "消毒管理DTO")
public class KitchenDisinfectDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "消毒时间", required = true)
    private LocalDateTime recordTime; // 消毒时间

    @ApiModelProperty(value = "照片", required = true)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img; // 照片

    @ApiModelProperty(value = "消毒项id")
    private Long itemId; // 消毒项

    @ApiModelProperty(value = "消毒项名")
    private String itemName;

    @ApiModelProperty(value = "合格标准")
    private String itemDesc;

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "责任人")
    private List<KitchenDisinfectDutyEmpDTO> kitchenDisinfectDutyEmpDTOS;

//    @ApiModelProperty(value = "责任人头像")
//    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
//    @JsonSerialize(using = ImgPair.ImgSerializer.class)
//    private String dutyEmpAvatar;

    //private String role;

    @ApiModelProperty(value = "检查人id", required = true)
    private Long auditorId; // 检查人id

    @ApiModelProperty(value = "检查人姓名", required = true)
    private String auditorName; // 检查人姓名

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "消毒要求")
    private String requirements;
}