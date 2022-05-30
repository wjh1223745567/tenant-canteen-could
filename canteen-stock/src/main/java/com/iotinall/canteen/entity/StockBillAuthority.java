package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 单据权限
 *
 * @author loki
 * @date 2020/09/14 10:31
 */
@Data
@Entity
@Table(name = "stock_bill_authority")
@EqualsAndHashCode(callSuper = true)
public class StockBillAuthority extends BaseEntity {
    /**
     * 对应单据ID
     */
    private Long billId;

    /**
     * 对应待办ID
     */
    private Long todoId;

    /**
     * 角色ID
     */
    private Long roleId;
}
