package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

/**
 * 库存相关代办列表
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_todo_list")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockTodo extends BaseEntity {
    /**
     * title
     */
    private String title;

    /**
     * 对应单据
     */
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private StockBill stockBill;

    /**
     * 待办类型
     * approval-审核
     * handle-经办
     */
    private String optType;

    /**
     * 当前任务ID
     */
    @ManyToOne
    @JoinColumn(name = "task_id")
    private StockFlwTask task;

    /**
     * 申请人
     */
    private Long applyUserId;

    /**
     * 申请人名称
     */
    private String applyUserName;

    /**
     * 待办接收人
     */
    private String receiverId;

    /**
     * 待办接收人名称
     */
    private String receiverName;

    /**
     * 状态
     * 1-待办
     * 2-已办
     */
    private Integer status;

    /**
     * 权限
     */
    @OneToMany(mappedBy = "todoId")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<StockBillAuthority> authorities;
}
