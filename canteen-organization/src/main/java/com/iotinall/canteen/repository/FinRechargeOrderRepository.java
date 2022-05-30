package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.FinRechargeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author WJH
 * @date 2019/11/513:40
 */
public interface FinRechargeOrderRepository extends JpaRepository<FinRechargeOrder, Long>, JpaSpecificationExecutor<FinRechargeOrder> {

    FinRechargeOrder findByOrderNo(String orderNo);

}
