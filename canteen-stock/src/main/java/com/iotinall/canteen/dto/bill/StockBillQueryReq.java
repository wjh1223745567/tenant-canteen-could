package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 单据分页列表请求参数
 *
 * @author loki
 * @date 2020/08/27 14:21
 */
@ApiModel(description = "入库出库单据列表请求参数")
@Data
public class StockBillQueryReq implements Serializable {
    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "姓名或者单据号")
    private String keyword;

    @ApiModelProperty(value = "单据类型 stock_in-采购入库 stock_in_back - 采购退货 stock_out-领用出库 stock_out_back-领用退库 stock_inventory 库存盘点")
    private String billType;
}
