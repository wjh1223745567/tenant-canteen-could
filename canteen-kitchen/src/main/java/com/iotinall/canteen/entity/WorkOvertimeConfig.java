package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 加班规则配置
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "work_overtime_config")
@EqualsAndHashCode(callSuper = true)
public class WorkOvertimeConfig extends BaseEntity {

    /**
     * 规则名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 工作日加班允许加班
     */
    @Column(nullable = false)
    private Boolean workOvertimeEnable;

    /**
     * 工作日允许加班
     */
    @Column(nullable = false)
    private Integer workOverType;

    /**
     * 下班多少分钟后计入加班
     */
    private Integer workOverStart;

    /**
     * 加班时长少于多少分钟不计入加班
     */
    private Integer workOverLess;

    /**
     * 休息日允许加班类型
     */
    @Column(nullable = false)
    private Boolean onRestDaysEnable;

    /**
     * 休息日允许加班类型
     *
     */
    @Column(nullable = false)
    private Integer onRestDaysType;

    private Integer onRestDaysStart;

    private Integer onRestDaysLess;

    /**
     * 节假日允许加班
     */
    @Column(nullable = false)
    private Boolean onHolidaysEnable;

    /**
     * 节假日允许加班类型
     *
     */
    @Column(nullable = false)
    private Integer onHolidaysType;

    private Integer onHolidaysStart;

    private Integer onHolidaysLess;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
