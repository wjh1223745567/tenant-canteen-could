package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.EmployeeWallet;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 钱包
 *
 * @author loki
 * @date 2020/04/27 16:02
 */
public interface EmployeeWalletRepository extends JpaRepositoryEnhance<EmployeeWallet, Long>, JpaSpecificationExecutor<EmployeeWallet> {
    /**
     * 增加余额
     *
     * @author loki
     * @date 2020/04/27 16:32
     */
    @Modifying
    @Query(value = "update EmployeeWallet p set p.balance = p.balance+:balance where p.id = :id")
    void addBalance(@Param(value = "id") Long id, @Param(value = "balance") BigDecimal balance);

    /**
     * 减少余额
     *
     * @author loki
     * @date 2020/04/27 16:32
     */
    @Modifying
    @Query(value = "update EmployeeWallet p set p.balance = p.balance-:balance where p.id = :id and p.balance>=:balance")
    int subtractBalance(@Param(value = "id") Long id, @Param(value = "balance") BigDecimal balance);

    /**
     * 修改余额
     *
     * @author loki
     * @date 2020/04/27 16:32
     */
    @Modifying
    @Query(value = "update EmployeeWallet p set p.balance = :balance where p.id = :id")
    void updateBalance(@Param(value = "id") Long id, @Param(value = "balance") BigDecimal balance);
}
