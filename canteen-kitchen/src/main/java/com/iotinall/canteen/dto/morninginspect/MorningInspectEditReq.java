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
@ApiModel(value = "编辑晨检请求")
public class MorningInspectEditReq {
    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "晨检用户不能为空")
    private Long empId;

    @NotNull(message = "晨检时间不能为空")
    private LocalDateTime recordTime;

    @NotNull(message = "检查结果不能为空")
    @IntEnum(message = "结果值不正确", values = {0, 1})
    private Integer state;

    @ApiModelProperty(value = "晨检备注")
    private String comments;

   // @NotBlank(message = "请上传照片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    private Long healthCodeId;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String healthCode;

    private Long itineraryCodeId;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String itineraryCode;
}
