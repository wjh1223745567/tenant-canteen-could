package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 出/入库申请表
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Getter
@Setter
@ApiModel(description = "单据")
@MappedSuperclass
public class BaseStockBillDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "单据号")
    private String billNo;

    @ApiModelProperty(value = "任务id")
    private Integer taskId;

    @ApiModelProperty(value = "采购入库的单据日期，领用出库的出库日期")
    private LocalDate billDate;

    @ApiModelProperty(value = "单据类型 stock_in-采购入库 stock_in_back - 采购退货 stock_out-领用出库 stock_out_back-领用退库 stock_inventory 库存盘点")
    private String billType;

    @ApiModelProperty(value = "状态：1-待审核  2-待验收 3-已完成 4-已拒绝  5-已取消")
    private Integer status;

    @ApiModelProperty(value = "结束日期")
    private LocalDate finishDate;

    @ApiModelProperty(value = "申请人信息")
    private StockBillApplyUserInfoDTO applyUserInfo;

    @ApiModelProperty(value = "审核人")
    private String auditUserName;

    @ApiModelProperty(value = "经办人名称")
    private String acceptanceUserName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作日志")
    private List<StockBillOperateLogDTO> operateLogs;

    @ApiModelProperty(value = "用户操作权限,OPTION_EDIT - 修改 OPTION_CANCEL - 取消  OPTION_AUDIT - 审核 OPTION_ACCEPTANCE - 验收 ")
    private List<String> options;

    @ApiModelProperty(value = "版本号")
    private Long version;
}
