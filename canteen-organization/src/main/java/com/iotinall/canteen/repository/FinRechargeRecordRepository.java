package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FinRechargeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

/**
 * fin_recharge_record Repository
 *
 * @author xin-bing
 * @date 2019-10-23 20:30:30
 */
public interface FinRechargeRecordRepository extends JpaRepository<FinRechargeRecord, Long>, JpaSpecificationExecutor<FinRechargeRecord> {
    /**
     * 统计充值金额
     *
     * @author loki
     * @date 2020/05/08 17:28
     */
    @Query(value = "select sum(record.amount)  from fin_recharge_record record where record.state = 1", nativeQuery = true)
    BigDecimal statRechargeAmount();

    FinRechargeRecord queryByRechargeRecordId(Long syncRecordId);
}