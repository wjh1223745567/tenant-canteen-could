package com.iotinall.canteen.dto.outbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 出库部门汇总
 *
 * @author loki
 * @date 2021/02/01 15:22
 */
@Data
@ApiModel(description = "出库汇总")
public class OrgStockOutQueryReq implements Serializable {

    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "部门ID")
    private Long orgId;

    @ApiModelProperty(value = "单据类型")
    private String billType;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
}
