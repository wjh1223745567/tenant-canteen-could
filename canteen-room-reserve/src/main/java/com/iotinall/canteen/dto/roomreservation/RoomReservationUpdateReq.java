package com.iotinall.canteen.dto.roomreservation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "包间管理修改参数")
public class RoomReservationUpdateReq {

    @ApiModelProperty(value = "包间Id")
    @NotNull(message = "请选择要修改的包间")
    private Long id;

    @ApiModelProperty(value = "包间名称",required = true)
    @NotBlank(message = "包间名称不能为空")
    private String roomName;


    @ApiModelProperty(value = "容纳人数",required = true)
    @NotNull(message = "容纳人数不能为空")
    private Integer people;

    @ApiModelProperty(value = "接待时间")
    @NotEmpty(message = "接待时间不能为空")
    @Valid
    private List<RoomReservationTimeUpdateReq> timeReqs;

    @ApiModelProperty(value = "包间位置",required = true)
    @NotBlank(message = "包间位置不能为空")
    private String roomAddress;

    @ApiModelProperty(value = "包间图片",required = true)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @NotBlank(message = "包间图片不能为空")
    private String roomPicture;

    private Long tenantOrgId;
}
