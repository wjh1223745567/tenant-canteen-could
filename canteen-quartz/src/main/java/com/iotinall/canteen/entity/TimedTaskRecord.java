package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "time_task_record")
public class TimedTaskRecord extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false, name = "timed_task_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private TimedTask timedTask;

    /**
     * 执行结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否执行成功
     */
    private Boolean success;

    /**
     * 执行失败日志
     */
    @Column(columnDefinition = "text")
    private String errorLog;

}
