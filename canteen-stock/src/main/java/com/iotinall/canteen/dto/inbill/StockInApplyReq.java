package com.iotinall.canteen.dto.inbill;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 申请入库请求参数
 *
 * @author loki
 * @date 2020/05/03 11:21
 */
@Data
@ApiModel(description = "采购入库申请请求参数")
public class StockInApplyReq implements Serializable {
    @ApiModelProperty("采购入库-单据日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "单据号")
    @NotBlank(message = "单据号不能为空")
    private String billNo;

    @ApiModelProperty(value = "供应商ID")
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "采购入库详情")
    @NotEmpty(message = "入库商品不能为空")
    @NotNull(message = "入库商品不能为空")
    private List<StockInDetailReq> details;

    @ApiModelProperty(value = "版本号")
    private Long version;

    @ApiModelProperty(value = "入库图片")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;
}
