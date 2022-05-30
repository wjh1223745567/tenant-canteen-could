package com.iotinall.canteen.dto.messprod;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 菜品原材料
 *
 * @author loki
 * @date 2020/03/25 11:10
 */
@Data
@ApiModel(value = "原材料")
@Accessors(chain = true)
public class MessProductMaterialDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "原材料名称")
    private String name;

    @ApiModelProperty(value = "原材料数量")
    private Long amount;

    @ApiModelProperty(value = "0-主料 1-辅料 2-调料")
    private Integer master;

    /**
     * 库存质量
     */
    private BigDecimal quality;

    @ApiModelProperty(value = "来源")
    private String source = "望家欢";

    @ApiModelProperty(value = "是否有验收报告 true -有 false -没有")
    private Boolean hasInspectionReport = true;

    @ApiModelProperty(value = "验收人")
    private String acceptor = "范阿香";

    @ApiModelProperty(value = "食物相克")
    private String restriction;
}
