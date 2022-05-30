package com.iotinall.canteen.dto.supplier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 供应商
 **/
@ApiModel(value = "供应商")
@Data
@Accessors(chain = true)
public class StockSupplierDTO {
    @ApiModelProperty(value = "供应商ID")
    private Long id;

    @ApiModelProperty(value = "供应商名称")
    private String name;

    @ApiModelProperty(value = "供应商编号")
    private String code;

    @ApiModelProperty(value = "信誉等级")
    private Integer credit;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系人地址")
    private String address;

    @ApiModelProperty(value = "供应商类型ID")
    private Long supplierTypeId;

    @ApiModelProperty(value = "供应商类型名称")
    private String supplierTypeName;

    @ApiModelProperty(value = "合作证明")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String cooperation;
}
