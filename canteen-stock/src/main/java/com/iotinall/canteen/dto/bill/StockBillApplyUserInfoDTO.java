package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 单据申请人信息
 *
 * @author loki
 * @date 2020/05/03 11:21
 */
@Data
@ApiModel(description = "单据申请人信息")
public class StockBillApplyUserInfoDTO implements Serializable {
    @ApiModelProperty(value = "申请人id")
    private Long id;

    @ApiModelProperty(value = "申请人名称")
    private String name;

    @ApiModelProperty(value = "申请人联系方式")
    private String contactMobile;

    @ApiModelProperty(value = "申请人部门")
    private String orgName;
}
