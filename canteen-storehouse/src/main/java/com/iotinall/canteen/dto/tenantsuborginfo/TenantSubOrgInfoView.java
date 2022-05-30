package com.iotinall.canteen.dto.tenantsuborginfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class TenantSubOrgInfoView {

    private Long id;

    private String name;

    /**
     * 容纳人数
     */
    private Integer capacity;

    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 面向人群
     */
    private String forTheCrowd;

    /**
     * 营业时间
     */
    private String businessHours;

    private BigDecimal distance;

    /**
     * 联系人
     */
    private String contactPerson;

    private String phone;

    private String remark;

}
