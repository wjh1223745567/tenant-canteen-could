package com.iotinall.canteen.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 操作日志记录表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "operation_log")
public class OperationLog extends BaseEntity{

    /**
     * 实体名称
     */
    @Column(nullable = false)
    private String className;

    @Column(columnDefinition = "text")
    private String data;

    /**
     * 操作类型
     */
    @Column(nullable = false)
    private Integer type;

}
