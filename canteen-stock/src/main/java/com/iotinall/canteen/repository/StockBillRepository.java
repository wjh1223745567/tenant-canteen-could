package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

/**
 * 单据持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockBillRepository extends JpaRepository<StockBill, Long>, JpaSpecificationExecutor<StockBill> {
    /**
     * 根据供应商统计单据
     */
    Long countBySupplier(StockSupplier supplier);

    /**
     * 根据单据号查找
     */
    StockBill findByBillNo(String billNo);

    /**
     * 获取用户申请的单据列表
     */
    List<StockBill> findByApplyUserIdAndStatusAndBillTypeOrderByCreateTimeDesc(Long userId,
                                                                               Integer status,
                                                                               String billType);

    List<StockBill> findByBillDateAndBillType(LocalDate billDate, String billType);
}
