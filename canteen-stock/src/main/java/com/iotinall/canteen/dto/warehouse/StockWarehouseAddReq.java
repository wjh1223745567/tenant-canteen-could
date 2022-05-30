package com.iotinall.canteen.dto.warehouse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加仓库请求参数
 **/
@Data
@ApiModel(description = "添加仓库请求参数")
public class StockWarehouseAddReq implements Serializable {

    @ApiModelProperty(value = "仓库名称", dataType = "string")
    @NotBlank(message = "仓库名称不能为空")
    private String name;

    @ApiModelProperty(value = "仓库地理位置", dataType = "string")
    @NotBlank(message = "仓库地址不能为空")
    private String address;

    @ApiModelProperty(value = "仓库图片", dataType = "string")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "仓库类型", dataType = "number")
    @NotNull(message = "仓库类型不能为空")
    private Long warehouseTypeId;

    @ApiModelProperty(value = "仓库管理员，多个以逗号拼接", dataType = "string")
    @NotBlank(message = "仓库管理员不能为空")
    private String managerIds;

    @ApiModelProperty(value = "备注", dataType = "string")
    private String remark;

    private Long parentId;
}
