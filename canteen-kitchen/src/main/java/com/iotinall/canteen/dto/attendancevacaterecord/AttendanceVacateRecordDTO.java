package com.iotinall.canteen.dto.attendancevacaterecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 *
 * @author xinbing
 * @date 2020-07-14 16:29:49
 */
@Data
@ApiModel(description = "请假记录DTO")
public class AttendanceVacateRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime; // 开始时间

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime; // 结束时间

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "请假原因")
    private String reason; // 请假原因

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion; // 审核意见

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "员工id")
    private Long empId; // 员工id

    @ApiModelProperty(value = "员工名称")
    private String empName;

    @ApiModelProperty(value = "员工头像")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String avatar;

    @ApiModelProperty(value = "员工职位")
    private String role; // 员工职位

    private Boolean used =false;

}