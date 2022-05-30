package com.iotinall.canteen.dto.morninginspect;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.jsr303.IntEnum;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "添加晨检记录")
public class MorningInspectAddReq {
    @NotNull(message = "晨检用户不能为空")
    @ApiModelProperty(value = "晨检用户id")
    private Long empId;

    @NotNull(message = "晨检时间不能为空")
    @ApiModelProperty(value = "晨检时间")
    private LocalDateTime recordTime;

    @NotNull(message = "检查结果不能为空")
    @IntEnum(message = "结果值不正确", values = {0, 1})
    @ApiModelProperty(value = "检查结果")
    private Integer state;

    @ApiModelProperty(value = "检查备注")
    private String comments;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String healthCode;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String itineraryCode;

    private Float temperature;
}
