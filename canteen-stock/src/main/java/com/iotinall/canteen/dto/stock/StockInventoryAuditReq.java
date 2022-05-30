package com.iotinall.canteen.dto.stock;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 盘点商品审核请求参数
 */
@Data
@ApiModel(value = "盘点审核请求参数")
public class StockInventoryAuditReq implements Serializable {

    @ApiModelProperty(value = "单据ID")
    @NotNull(message = "单据ID不能为空")
    private String billNo;

    @ApiModelProperty(value = "操作类型 type = 0 拒绝 1同意")
    @NotNull(message = "操作类型不能为空")
    private Integer optType;

    @ApiModelProperty("意见")
    private String remark;

    @ApiModelProperty(value = "")
    private Long version;
}
