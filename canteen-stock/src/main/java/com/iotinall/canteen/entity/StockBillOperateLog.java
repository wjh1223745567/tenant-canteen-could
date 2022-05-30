package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 库存工单操作日志
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_bill_operate_log")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockBillOperateLog extends BaseEntity {
    /**
     * 操作人ID
     */
    private Long optUserId;

    /**
     * 操作人名称
     */
    private String optUserName;

    /**
     * 操作类型
     * apply-申请
     * audit-审核
     * acceptance-验收 //经办
     * cancel - 取消
     */
    private String optType;

    /**
     * 审核结果
     * 1-同意
     * 0-拒绝
     */
    private Integer auditResult;

    /**
     * 任务
     */
    @ManyToOne
    @JoinColumn(name = "task_id")
    private StockFlwTask task;

    /**
     * 单据ID
     */
    @OneToOne
    @JoinColumn(name = "bill_id")
    private StockBill bill;
}
