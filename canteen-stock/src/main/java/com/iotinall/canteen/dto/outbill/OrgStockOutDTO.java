package com.iotinall.canteen.dto.outbill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.utils.Decimal2Serializer;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

/**
 * 出库部门汇总
 *
 * @author loki
 * @date 2021/02/01 15:22
 */
@ApiModel(description = "出库汇总")
public interface OrgStockOutDTO {
    Long getGoodsId();

    String getGoodsName();

    String getUnit();

    Long getSupplierId();

    String getSupplierName();

    Long getOrgId();

    String getOrgName();

    @JsonSerialize(using = Decimal2Serializer.class)
    BigDecimal getQuantity();

    @JsonSerialize(using = Decimal2Serializer.class)
    BigDecimal getPrice();
}
