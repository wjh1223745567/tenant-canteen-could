package com.iotinall.canteen.dto.item;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "项目")
public class ItemDTO {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "项目名称")
    private String name;
//    @ApiModelProperty(value = "描述")
//    private String description;
    @ApiModelProperty(value = "排序")
    private Integer seq;

    @ApiModelProperty(value = "责任人")
    private List<ItemDutyEmpDTO> itemDutyEmpDTO;
//    @ApiModelProperty(value = "责任人Id")
//    private Long dutyEmpId;
//    @ApiModelProperty(value = "责任人姓名")
//    private String dutyEmpName;

    @ApiModelProperty(value = "关联摄像头")
    private List<ItemCameraDTO> cameraDTOS;

    @ApiModelProperty(value = "检查时间")
    private List<String> checkTimes;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "要求")
    private String requirements;

    @ApiModelProperty(value = "备注")
    private String comment;
}
