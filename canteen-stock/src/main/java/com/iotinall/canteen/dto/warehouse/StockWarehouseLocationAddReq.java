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
 * 添加位置请求参数
 **/
@Data
@ApiModel(description = "添加位置请求参数")
public class StockWarehouseLocationAddReq implements Serializable {
    @ApiModelProperty(value = "位置名称", dataType = "string")
    @NotBlank(message = "位置名称不能为空")
    private String name;


    @ApiModelProperty(value = "上级位置", dataType = "number")
    @NotNull(message = "上级位置不能为空")
    private Long parentId;

    @ApiModelProperty(value = "位置图片", dataType = "string")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String imgUrl;
}
