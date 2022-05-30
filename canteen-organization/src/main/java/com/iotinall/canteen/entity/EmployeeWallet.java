package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 员工钱包
 *
 * @author loki
 * @date 2020/04/27 15:49
 */
@NoArgsConstructor
@Entity
@Table(name = "org_employee_wallet")
public class EmployeeWallet extends BaseEntity {

    /**
     * 支付秘钥
     */
    @Column(length = 200)
    @Getter
    @Setter
    private String payPassword;

    /**
     * 钱包余额
     */
    @Column(nullable = false)
    @Getter
    private BigDecimal balance;

    public EmployeeWallet(BigDecimal initBalance) {
        this.balance = initBalance;
    }
}
