package com.iotinall.canteen.dto.inbill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 入库验收请求参数
 *
 * @author loki
 * @date 2020/09/01 20:01
 */
@Data
@ApiModel(description = "入库验收请求参数")
public class StockInAcceptanceReq implements Serializable {
    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "货品ID")
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    @ApiModelProperty(value = "生产日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate productionDate;

    @ApiModelProperty(value = "保质期,整形数字")
    private Integer shelfLife;

    @ApiModelProperty(value = "保质期单位 0-年 1-月 2-日")
    private Integer shelfLifeUnit;

    @ApiModelProperty(value = "检测报告")
    private Boolean inspectionReport;

    @ApiModelProperty(value = "实际入库数量")
    @NotNull(message = "请填写实际入库数量")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "入库图片")
    private MultipartFile file;

    @ApiModelProperty(value = "版本号")
    private Long version;
}
