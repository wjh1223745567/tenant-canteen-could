package com.iotinall.canteen.dto.morninginspect;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "晨检")
public class MorningInspectDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "员工id")
    private Long empId;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "头像")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String avatar;

    @ApiModelProperty(value = "职位")
    private String role;

    @ApiModelProperty(value = "检查人id")
    private Long auditorId;

    @ApiModelProperty(value = "检查人名称")
    private String auditorName;

    @ApiModelProperty(value = "晨检时间")
    private LocalDateTime recordTime;

    @ApiModelProperty(value = "检查结果")
    private Integer state;

    @ApiModelProperty(value = "晨检备注")
    private String comments;

    /**
     * 体温
     */
    @ApiModelProperty(value = "体温")
    private Float temperature;

    @ApiModelProperty(value = "记录照片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String img;
}
