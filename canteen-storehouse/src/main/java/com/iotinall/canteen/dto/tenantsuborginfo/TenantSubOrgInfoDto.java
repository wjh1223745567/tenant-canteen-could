package com.iotinall.canteen.dto.tenantsuborginfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class TenantSubOrgInfoDto {

    private Long id;

    private String name;

    /**
     * 容纳人数
     */
    private Integer capacity;

    private String address;

    private String remark;

    /**
     * 对应数据源
     */
    private String dataSource;

    /**
     * 营业时间
     */
    private String businessHours;

    /**
     * 距离
     */
    private BigDecimal distance;

}
