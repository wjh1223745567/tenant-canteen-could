package com.iotinall.canteen.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存工单操作日志
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@ApiModel(value = "操作日志")
@Data
public class StockBillOperateLogDTO {
    @ApiModelProperty(value = "操作人id")
    private Long optUserId;

    @ApiModelProperty(value = "操作人名称")
    private String optUserName;

    @ApiModelProperty(value = "操作类型 apply-申请，audit-审核，acceptance-验收 cancel-取消")
    private String optType;

    @ApiModelProperty(value = "审核意见")
    private String remark;

    @ApiModelProperty(value = "审批结果 0-拒绝 1-同意")
    private Integer auditResult;

    @ApiModelProperty(value = "任务名称 apply-申请，audit-审核，acceptance-验收 end-结束  如果值为end，则根据optType 显示已验收或者已取消")
    private String taskDefine;

    @ApiModelProperty(value = "任务id")
    private Long taskId;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
}
