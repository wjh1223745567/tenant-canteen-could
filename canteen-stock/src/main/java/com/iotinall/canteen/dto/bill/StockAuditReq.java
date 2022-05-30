package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 申请入库请求参数
 *
 * @author loki
 * @date 2020/05/03 11:21
 */
@Data
@ApiModel(description = "审批单据请求参数")
public class StockAuditReq implements Serializable {
    @ApiModelProperty(value = "单据ID")
    @NotNull(message = "单据ID不能为空")
    private String billNo;

    @ApiModelProperty(value = "操作类型 type = 0 拒绝 1同意")
    @NotNull(message = "操作类型不能为空")
    private Integer optType;

    @ApiModelProperty(value = "意见")
    private String remark;

    @ApiModelProperty(value = "版本号")
    private Long version;
}
