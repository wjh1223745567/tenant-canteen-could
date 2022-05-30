package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 单据凭证，验收报告
 */
@Data
@Entity
@Table(name = "stock_certificate_and_inspection_report")
@EqualsAndHashCode(callSuper = true)
public class StockCertificateAndInspectionReport extends BaseEntity {
    /**
     * 对应单据
     */
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private StockBill stockBill;

    /**
     * 照片路径
     */
    private String imgUrl;
}
