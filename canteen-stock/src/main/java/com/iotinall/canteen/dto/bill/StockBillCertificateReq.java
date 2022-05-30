package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel(description = "订单上传图片参数")
public class StockBillCertificateReq {

    @ApiModelProperty("单据号")
    @NotBlank(message = "单据号不能为空")
    private String billNo;

    @ApiModelProperty(value = "入库图片")
    private String imgs;
}
