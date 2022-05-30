package com.iotinall.canteen.dto.tenantsuborginfo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TenantSubOrgInfoReq {

    @NotNull
    private Long id;

    @NotBlank
    @Length(max = 60)
    private String name;

    /**
     * 容纳人数
     */
    @NotNull
    private Integer capacity;

    @NotBlank
    private String address;

    /**
     * 经度
     */
    @NotNull
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @NotNull
    private BigDecimal latitude;

    /**
     * 面向人群
     */
    @Length(max = 255)
    private String forTheCrowd;

    /**
     * 联系人
     */
    @Length(max = 255)
    private String contactPerson;

    @Length(max = 20)
    private String phone;

    @Length(max = 2000)
    private String remark;

}
