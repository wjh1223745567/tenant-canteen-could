package com.iotinall.canteen.dto.inbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购退货请求参数
 *
 * @author loki
 * @date 2020/05/03 11:21
 */
@Data
@ApiModel(description = "采购退货请求参数")
public class StockInBackDetailApplyReq implements Serializable {
    @ApiModelProperty(value = "退货商品ID")
    @NotNull(message = "要退货的货品ID不能为空")
    private Long goodsId;

    @ApiModelProperty(value = "退库商品对应的入库明细ID")
    @NotBlank(message = "退库商品对应的入库明细ID")
    private Long inDetailId;

    @ApiModelProperty(value = "退库数量")
    private BigDecimal backAmount;

    @ApiModelProperty(value = "退库当时的库存数")
    private BigDecimal stockAmount;
}
