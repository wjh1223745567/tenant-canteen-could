package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 出入库明细列表（采购入库，采购退货，领用申请，领用退库） 请求参数
 *
 * @author loki
 * @date 2020/08/25 20:26
 */
@Data
@ApiModel(description = "出入库明细查询请求参数")
public class StockBillDetailQueryReq implements Serializable {

    @ApiModelProperty(value = "商品名称")
    private String keyword;

    @ApiModelProperty(value = "商品类型ID")
    private Long goodsTypeId;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "单据类型，入库：stock_in,退库：stock_out_back 出库：stock_out,退货：stock_in_bak", allowableValues = "stock_in,stock_out_back,stock_out,stock_in_bak")
    @NotEmpty(message = "单据类型不能为空")
    private List<String> billType;

    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "部门ID")
    private Long orgId;
}
