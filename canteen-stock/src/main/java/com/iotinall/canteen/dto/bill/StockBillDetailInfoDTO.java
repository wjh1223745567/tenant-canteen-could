package com.iotinall.canteen.dto.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 出入库明细（采购入库、领用出库、领用退库）
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@ApiModel(description = "出入库明细")
public class StockBillDetailInfoDTO implements Serializable {
    private Long billDetailId;

    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "单据日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "规格型号")
    private String specs;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "入库图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "商品类型名称")
    private String goodsTypeName;

    @ApiModelProperty(value = "商品仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "入库、出库数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "实际入库、出库数量")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal realAmount;

    @ApiModelProperty(value = "单价")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal price;

    @ApiModelProperty(value = "单据类型")
    private String billType;

    @ApiModelProperty(value = "金额")
    @JsonSerialize(using = Decimal2Serializer.class)
    private BigDecimal money;

    @ApiModelProperty(value = "申请人名称")
    private String applyUserName;

    @ApiModelProperty(value = "组织机构ID")
    private Long orgId;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "剩余库存数量")
    private BigDecimal leftAmount;
}
