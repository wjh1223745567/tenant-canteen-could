package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockCertificateAndInspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 单据验收报告等持久化类
 *
 * @author loki
 * @date 2021/6/10 14:34
 **/
public interface StockCertificateAndInspectionReportRepository extends JpaRepository<StockCertificateAndInspectionReport, Long>
        , JpaSpecificationExecutor<StockCertificateAndInspectionReport> {
    /**
     * 获取单据上传的图片文件
     */
    List<StockCertificateAndInspectionReport> findByStockBill(StockBill bill);
}
