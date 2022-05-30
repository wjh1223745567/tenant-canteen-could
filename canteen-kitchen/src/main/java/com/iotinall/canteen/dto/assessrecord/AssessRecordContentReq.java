package com.iotinall.canteen.dto.assessrecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 考核内容查询
 */
@Data
public class AssessRecordContentReq {

    @ApiModelProperty(value = "开始时间，年月日转换后的时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "请先选取时间")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束时间，", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "请先选取时间")
    private LocalDate endDate;

    @ApiModelProperty(value = "类型，0月度， 1季度， 2年度")
    @NotNull(message = "季度类型不能为空")
    private Integer typ;

    @ApiModelProperty(value = "考核人员IDS")
    @NotEmpty(message = "请选择考核人员")
    private List<Long> empIds;

    @ApiModelProperty(value = "考核内容 菜单  0考勤 1晨检管理 2留样管理 3消毒管理 4餐厨垃圾 5设备设施 6环境卫生")
    private Integer contentType;

}
