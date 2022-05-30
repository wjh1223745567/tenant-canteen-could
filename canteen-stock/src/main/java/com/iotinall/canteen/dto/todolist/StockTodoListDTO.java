package com.iotinall.canteen.dto.todolist;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 库存相关代办列表
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@ApiModel(value = "待办对象")
public class StockTodoListDTO implements Serializable {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "对应单据ID")
    private Long billId;

    @ApiModelProperty(value = "冗余单据编号")
    private String billNo;

    @ApiModelProperty(value = "单据日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "单据类型 stock_in-采购入库 stock_in_back - 采购退货 stock_out-领用出库 stock_out_back-领用退库 stock_inventory 库存盘点")
    private String billType;

    @ApiModelProperty(value = "操作类型 approval-审核 acceptance - 验收")
    private String optType;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "申请人名称")
    private String applyUserName;

    @ApiModelProperty(value = "备注")
    private String remark;
}
