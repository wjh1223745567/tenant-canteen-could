package com.iotinall.canteen.dto.supplier;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 供应商修改请求参数
 */
@Data
@ApiModel(value = "供应商修改请求参数")
public class StockSupplierEditReq {
    @ApiModelProperty(value = "供应商Id")
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "供应商名称")
    @NotBlank(message = "供应商名称不能为空")
    private String name;

    @ApiModelProperty(value = "供应商类型")
    @NotNull(message = "供应商类型不能为空")
    private Long supplierTypeId;

    @ApiModelProperty(value = "供应商信誉度")
    @NotBlank(message = "信誉度不能为空")
    private Integer credit;

    @ApiModelProperty(value = "供应商联系人")
    @NotBlank(message = "联系人不能为空")
    private String contact;

    @ApiModelProperty(value = "供应商联系电话")
    @NotBlank(message = "联系电话不能为空")
    private String contactNumber;

    @ApiModelProperty(value = "供应商联系地址")
    @NotBlank(message = "联系地址不能为空")
    private String address;

    @ApiModelProperty(value = "供应商合作证明")
    @NotBlank(message = "合作证明图片不能为空")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String cooperation;

}
