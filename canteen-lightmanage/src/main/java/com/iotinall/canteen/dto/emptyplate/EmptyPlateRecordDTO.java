package com.iotinall.canteen.dto.emptyplate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author joelau
 * @date 2021/05/25 14:33
 */
@Data
@ApiModel(description = "返回fin_recharge_record结果")
public class EmptyPlateRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "记录图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "记录时间")
    private LocalDateTime recordTime;

    @ApiModelProperty(value = "餐次名")
    private String mealTypeName;

    @ApiModelProperty(value = "违规人员姓名")
    private Set<String> violatorsName;
}
