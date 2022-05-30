package com.iotinall.canteen.dto.warehouse;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 仓库
 **/
@Data
@Accessors(chain = true)
@ApiModel(value = "仓库对象")
public class StockWarehouseDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "仓库全称")
    private String fullName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "仓库图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "仓库类型")
    private StockWarehouseTypeDTO warehouseType;

    private Integer type;

    @ApiModelProperty(value = "仓库管理员")
    private List<StockWarehouseManagerDTO> managers;

    @ApiModelProperty(value = "父节点ID")
    private Long parentId;

    @ApiModelProperty(value = "子集")
    private List<StockWarehouseDTO> children;

    @ApiModelProperty(value = "备注")
    private String remark;
}
