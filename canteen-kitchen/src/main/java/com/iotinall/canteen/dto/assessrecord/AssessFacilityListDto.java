package com.iotinall.canteen.dto.assessrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.garbage.GarbageDutyEmpDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "考核记录---设备设施")
public class AssessFacilityListDto implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "检查区域id")
    private Long itemId;

    @ApiModelProperty(value = "检查区域名称")
    private String itemName;

    @ApiModelProperty(value = "检查区域要求")
    private String itemDesc;

    @ApiModelProperty(value = "图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime recordTime;

    @ApiModelProperty(value = "责任人/考核人")
    private List<GarbageDutyEmpDTO> garbageDutyEmpDTOList;

}
