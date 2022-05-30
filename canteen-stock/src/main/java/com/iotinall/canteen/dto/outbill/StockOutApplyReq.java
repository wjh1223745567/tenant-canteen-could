package com.iotinall.canteen.dto.outbill;

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
@ApiModel(value = "领用出库申请请求参数")
@Data
public class StockOutApplyReq implements Serializable {
    @ApiModelProperty("出库日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "单据号")
    @NotBlank(message = "单据号不能为空")
    private String billNo;

    @ApiModelProperty(value = "申请人")
    private Long applyUserId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "领用出库详情")
    @NotEmpty(message = "至少选择1件出库商品")
    @NotNull(message = "至少选择1件出库商品")
    private List<StockOutGoodsDetailReq> details;
}
