package com.iotinall.canteen.dto.assessrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.sample.KitchenSampleDutyEmpDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "考核记录---留样管理")
public class AssessSampleListDto implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "餐次（1、2、4）")
    private Integer mealType;

//    @ApiModelProperty(value = "食物")
//    private List<KitchenSampleAddReq.Foods> foods;

    @ApiModelProperty(value = "留样要求")
    private String requirements;

    @ApiModelProperty(value = "图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private List<String> img;

    @ApiModelProperty(value = "记录时间")
    private LocalDateTime recordTime;

    @ApiModelProperty(value = "留样责任人")
    private List<KitchenSampleDutyEmpDTO> kitchenSampleDutyEmpDTOS;

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "留样备注")
    private String comments; // 留样备注


}
