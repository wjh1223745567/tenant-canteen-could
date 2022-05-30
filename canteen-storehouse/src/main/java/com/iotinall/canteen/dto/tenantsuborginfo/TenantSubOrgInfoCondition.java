package com.iotinall.canteen.dto.tenantsuborginfo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TenantSubOrgInfoCondition {

    private String keyword;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

}
