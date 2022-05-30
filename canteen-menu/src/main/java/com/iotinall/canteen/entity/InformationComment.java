package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author WJH
 * @date 2019/11/1518:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table
@Accessors(chain = true)
public class InformationComment extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "information_id", foreignKey = @ForeignKey(name = "null"), nullable = false)
    private Information information;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(nullable = false, length = 1000)
    private String content;

    private Integer praise;

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    private String praiseEmp;

    @Column(nullable = false)
    private Boolean anonymous;
}
