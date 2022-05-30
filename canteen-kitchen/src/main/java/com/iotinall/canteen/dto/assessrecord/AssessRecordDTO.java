package com.iotinall.canteen.dto.assessrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "考核记录")
public class AssessRecordDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String empAvatar;

    @ApiModelProperty(value = "员工id")
    private Long empId;

    @ApiModelProperty(value = "员工姓名")
    private String empName;

    @ApiModelProperty(value = "职位")
    private String role;

    @ApiModelProperty(value = "考核类型", example = "0,1,2")
    private Integer typ;

    @ApiModelProperty(value = "开始时间")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束时间")
    private LocalDate endDate;

    @ApiModelProperty(value = "考核时间")
    private String checkingTime;

    @ApiModelProperty(value = "考核结果id")
    private Long itemId;

    @ApiModelProperty(value = "考核结果名称")
    private String itemName;

    @ApiModelProperty(value = "考核结果描述")
    private String itemDesc;

    @ApiModelProperty(value = "考核评语")
    private String comments;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 是否是自己的数据
     */
    @ApiModelProperty(value = "是否是自己的数据")
    private Boolean used = false;

}
