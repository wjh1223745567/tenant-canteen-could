package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 入/出库审批/经办操作人配置表
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_flw_task")
@ToString(exclude = {"flwConfig"})
@EqualsAndHashCode(exclude = {"flwConfig"}, callSuper = false)
@JsonIgnoreProperties(value = {"flwConfig"})
@Accessors(chain = true)
public class StockFlwTask extends BaseEntity {
    /**
     * 上一任务ID
     */
    private Integer preTaskId;

    /**
     * 当前任务ID
     */
    private Integer taskId;

    /**
     * 下一任务ID
     */
    private Integer nextTaskId;

    /**
     * 任务类型
     * request - 申请
     * approval-审核(存在多个)
     * handle-经办
     */
    private String taskDefine;

    /**
     * 处理人，多个以逗号拼接
     */
    private String handlerId;

    /**
     * 处理人类型
     */
    private String handlerType;

    /**
     * 流程配置
     */
    @OneToOne
    @JoinColumn(name = "flw_config_id")
    private StockFlwConfig flwConfig;

    /**
     * 版本号
     * 对应流程配置的版本号，单据的版本号与这里的版本号对应
     */
    private Long version;
}
