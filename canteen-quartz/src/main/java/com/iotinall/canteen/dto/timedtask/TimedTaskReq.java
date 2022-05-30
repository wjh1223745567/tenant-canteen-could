package com.iotinall.canteen.dto.timedtask;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TimedTaskReq {

    private Long id;

    /**
     * 定时任务
     */
    @Length(max = 30)
    @NotBlank
    private String name;

    /**
     * 任务类型
     * @see com.iotinall.canteen.constant.JobTypeEnum
     */
    @NotNull
    private Integer type;

    /**
     * 时间表达式
     */
    @NotBlank
    private String corn;

    /**
     * 备注说明
     */
    private String remark;

}
