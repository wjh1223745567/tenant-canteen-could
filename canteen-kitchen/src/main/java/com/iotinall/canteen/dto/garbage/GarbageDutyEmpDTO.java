package com.iotinall.canteen.dto.garbage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "多个厨余垃圾责任人")
public class GarbageDutyEmpDTO {
    @ApiModelProperty(value = "责任人Id")
    private Long dutyEmpId;

    @ApiModelProperty(value = "责任人姓名")
    private String dutyEmpName;

    @ApiModelProperty(value = "责任人头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String dutyAvatar;

    private String role;

}
