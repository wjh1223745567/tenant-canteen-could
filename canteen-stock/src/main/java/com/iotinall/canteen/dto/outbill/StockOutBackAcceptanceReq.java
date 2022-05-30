package com.iotinall.canteen.dto.outbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 领用出库 验收请求参数
 *
 * @author loki
 * @date 2020/09/01 20:01
 */
@Data
@ApiModel(description = "领用出库 验收请求参数")
public class StockOutBackAcceptanceReq implements Serializable {
    @ApiModelProperty(value = "退库单据号")
    private String billNo;

    @ApiModelProperty(value = "货品ID")
    private Long goodsId;

    @ApiModelProperty(value = "退库图片")
    private MultipartFile file;

    @ApiModelProperty(value = "实际退库数量")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "版本号")
    private Long version;
}
