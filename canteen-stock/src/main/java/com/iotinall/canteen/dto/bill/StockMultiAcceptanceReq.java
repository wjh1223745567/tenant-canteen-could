package com.iotinall.canteen.dto.bill;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 一键验收请求参数
 *
 * @author loki
 * @date 2021/01/04 20:45
 */
@Data
public class StockMultiAcceptanceReq implements Serializable {
    private String billNo;
    private Long version;
    private Set<Long> detailIds;
}
