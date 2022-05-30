package com.iotinall.canteen.dto.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存汇总
 *
 * @author loki
 * @date 2020/09/12 11:00
 */
@Data
@ApiModel(description = "电子秤返回参数")
public class StockElectronicDTO implements Serializable {
    @ApiModelProperty(value = "验收图片")
    private String imgUrl;

    @ApiModelProperty(value = "每个商品验收状态 1-图片上传失败 2-经办失败")
    private Integer acceptStatus;

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
    private BigDecimal realAmount;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "单据号")
    private String billNo;
}
