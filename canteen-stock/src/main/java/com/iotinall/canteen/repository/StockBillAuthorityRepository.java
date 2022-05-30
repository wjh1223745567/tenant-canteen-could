package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockBillAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 单据权限持久化类
 *
 * @author loki
 * @date 2021/6/3 20:44
 **/
public interface StockBillAuthorityRepository extends JpaRepository<StockBillAuthority, Long>, JpaSpecificationExecutor<StockBillAuthority> {
}
