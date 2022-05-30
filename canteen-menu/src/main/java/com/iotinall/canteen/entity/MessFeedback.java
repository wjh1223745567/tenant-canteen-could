package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 意见反馈
 *
 * @author xin-bing
 * @date 10/22/2019 20:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Accessors(chain = true)
@Table(name = "mess_feedback")
public class MessFeedback extends BaseEntity {

    @Column(nullable = false)
    private String content; // 内容

    @Column(name = "emp_id", nullable = false)
    private Long employeeId; // 反馈人

    @Column(nullable = false)
    private Boolean anonymous; // 是否匿名

    @Column(nullable = false)
    private String feedType; // 反馈类型，字典表的值

    @Column(nullable = false)
    private Integer status; // 状态， 0， 1， 2

    @Column
    private LocalDateTime handleTime; // 处理时间

    @Column
    private String handleOpinion; // 处理意见

    @Column
    private String handler; // 处理人

}
