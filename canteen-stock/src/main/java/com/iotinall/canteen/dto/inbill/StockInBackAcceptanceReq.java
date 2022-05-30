package com.iotinall.canteen.dto.inbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退货验收请求参数
 *
 * @author loki
 * @date 2020/09/01 20:01
 */
@Data
@ApiModel(description = "退货验收请求参数")
public class StockInBackAcceptanceReq implements Serializable {
    @ApiModelProperty(value = "退货单据号")
    private String billNo;

    @ApiModelProperty(value = "对应入库单据号")
    private String inBillNo;

    @ApiModelProperty(value = "货品ID")
    private Long goodsId;

    @ApiModelProperty(value = "实际退货数量")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "退货图片")
    private MultipartFile file;

    @ApiModelProperty(value = "版本号")
    private Long version;
}
