package com.iotinall.canteen.dto.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "盘点申请请求参数")
public class StockInventoryApplyReq implements Serializable {

    @ApiModelProperty(value = "单据号")
    @NotBlank(message = "单据号不能为空")
    private String billNo;

    @ApiModelProperty(value = "仓库ID")
    @NotNull(message = "仓库不能为空")
    private Long warehouseId;

    @ApiModelProperty(value = "单据日期")
    @NotNull(message = "单据日期不能为空")
    private LocalDate billDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "商品明细")
    List<StockInventoryApplyGoodsReq> details;
}
