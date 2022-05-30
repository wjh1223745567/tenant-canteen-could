package com.iotinall.canteen.dto.assessrecord;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "")
public class AssessMorningDto {

    @ApiModelProperty(value = "考核人Id")
    private Long empId;

    @ApiModelProperty(value = "考核人姓名")
    private String empName;

    @ApiModelProperty(value = "需晨检")
    private Integer frequency;

    @ApiModelProperty(value = "晨检率")
    private BigDecimal morningInspectionRate;

    @ApiModelProperty(value = "合格次数")
    private Integer qualifiedTimes;

    @ApiModelProperty(value = "合格率")
    private BigDecimal passRate;

    @ApiModelProperty(value = "不合格次数")
    private Integer timesOfDisqualification;

    @ApiModelProperty(value = "不合格率")
    private BigDecimal unqualifiedRate;

}
