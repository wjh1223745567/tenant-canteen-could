package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constant.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * 出/入库申请表
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@ToString(exclude = {"details"})
@JsonIgnoreProperties(value = {"details"})
@EqualsAndHashCode(exclude = {"details"}, callSuper = true)
@Table(name = "stock_bill")
@Accessors(chain = true)
public class StockBill extends BaseEntity {
    /**
     * 单据号
     */
    @Column(length = 32, unique = true)
    private String billNo;

    /**
     * 单据日期
     */
    private LocalDate billDate;

    /**
     * 类型
     * 单据类型 stock_in-采购入库 stock_in_back - 采购退货 stock_out-领用出库 stock_out_back-领用退库 stock_inventory 库存盘点
     */
    private String billType;

    /**
     * 单据状态
     *
     * @see Constants.BILL_STATUS
     */
    private Integer status;

    /**
     * 出入库详情
     */
    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER)
    @OrderBy("seq asc")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<StockBillDetail> details;

    /**
     * 供应商
     */
    @OneToOne
    @JoinColumn(name = "supplier_id", foreignKey = @ForeignKey(name = "null"))
    @NotFound(action = NotFoundAction.IGNORE)
    private StockSupplier supplier;

    /**
     * 结束日期
     */
    private LocalDate finishDate;

    /**
     * 申请人
     */
    @Column(name = "apply_user_id")
    private Long applyUserId;

    /**
     * 申请人
     */
    @Column(name = "apply_user_name")
    private String applyUserName;

    /**
     * 申请人部门ID
     */
    private Long applyUserOrgId;

    /**
     * 申请人部门
     */
    @Column(name = "apply_user_org_name")
    private String applyUserOrgName;

    /**
     * 审批人
     */
    @Column(name = "audit_user_id")
    private String auditUserId;

    /**
     * 审批人
     */
    @Column(name = "audit_user_name")
    private String auditUserName;

    /**
     * 经办人
     */
    @Column(name = "acceptance_user_id")
    private Long acceptanceUserId;

    /**
     * 经办人
     */
    @Column(name = "acceptance_user_name")
    private String acceptanceUserName;

    /**
     * 流程任务ID
     */
    @ManyToOne
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "null"))
    private StockFlwTask task;

    /**
     * 单据权限
     */
    @OneToMany(mappedBy = "billId", fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<StockBillAuthority> authorities;

    /**
     * 单据操作日志
     */
    @OneToMany
    private Set<StockBillOperateLog> logs;

    /**
     * 单据版本
     * 防止多个客户端同时对同一单据进行编辑
     */
    @Column(columnDefinition = "bigint")
    private Long version;

    /**
     * 领用退库，对应的出库单据
     */
    @OneToOne
    @JoinColumn(name = "out_bill_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private StockBill outBill;

    /**
     * 仓库
     *
     * @author loki
     * @date 2021/7/30 17:46
     **/
    @OneToOne
    @JoinColumn(name = "warehouse_id")
    private StockWarehouse warehouse;
}
