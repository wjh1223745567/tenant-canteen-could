package com.iotinall.canteen.dto.nutritionenc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 原材料 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 11:10
 */
@Data
@ApiModel(value = "原材料")
@Accessors(chain = true)
public class FeignSysMaterialReq implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "原材料名称")
    private String name;

    @ApiModelProperty(value = "原材料数量")
    private BigDecimal amount;

    @ApiModelProperty(value = "0-主料 1-辅料 2-调料")
    private Integer master;

    @ApiModelProperty(value = "来源")
    private String source = "望家欢";

    @ApiModelProperty(value = "是否有验收报告 true -有 false -没有")
    private Boolean hasInspectionReport = true;

    @ApiModelProperty(value = "验收人")
    private String acceptor = "范阿香";
}
