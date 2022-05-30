package com.iotinall.canteen.dto.electronic;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 电子秤单据对象
 *
 * @author loki
 * @date 2021/05/27 17:15
 */
@Data
public class ElectronicBillDTO implements Serializable {
    private Long billId;
    private String billNo;
    private LocalDate billDate;
    private String supplierName;
    private Long version;

    List<ElectronicGoodsDTO> goodsList;
}
