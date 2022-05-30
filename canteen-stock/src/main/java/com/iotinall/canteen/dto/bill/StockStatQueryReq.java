package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 库存汇总请求参数
 *
 * @author loki
 * @date 2020/09/11 18:03
 */
@Data
@ApiModel(value = "库存汇总请求参数")
public class StockStatQueryReq implements Serializable {
    @ApiModelProperty(value = "查询月份")
    @NotNull(message = "查询月份不能为空")
    private LocalDate month;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "商品类型ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品名称")
    private String keyword;
}
