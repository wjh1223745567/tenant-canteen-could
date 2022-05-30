package com.iotinall.canteen.entity;


import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "time_task")
public class TimedTask extends BaseEntity {

    /**
     * 定时任务
     */
    @Column(nullable = false, length = 30)
    private String name;

    /**
     * 任务类型
     * @see com.iotinall.canteen.constant.JobTypeEnum
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 时间表达式
     */
    @Column(nullable = false)
    private String corn;

    /**
     * 最后一次执行时间
     */
    private LocalDateTime lastExecuteTime;

    /**
     * 最后一次是否执行成功
     */
    private Boolean lastSuccess;

}
