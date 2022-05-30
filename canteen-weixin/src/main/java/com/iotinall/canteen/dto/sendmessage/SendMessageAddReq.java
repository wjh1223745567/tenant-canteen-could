package com.iotinall.canteen.dto.sendmessage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SendMessageAddReq {

    @ApiModelProperty(value = "提示内容")
    @NotBlank(message = "提示内容不能为空")
    private String content;

    @NotNull(message = "提示类型不能为空")
    @ApiModelProperty(value = "提示类型 * " +
            "       1-早餐\n" +
            "     * 2-午餐\n" +
            "     * 3-晚餐\n" +
            "     * 4-余额不足\n" +
            "     * 5-点评和饮食记录\n" +
            "     * 6-商城取货\n" +
            "     * 7-反馈\n" +
            "     * 8-进销存采购入库申请\n" +
            "     * 9-进销存采购退货申请\n" +
            "     * 10-进销存领用出库申请\n" +
            "     * 11-进销存领用退库申请\n" +
            "     * 12-进销存采购入库审核\n" +
            "     * 13-进销存采购退货审核\n" +
            "     * 14-进销存领用出库审核\n" +
            "     * 15-进销存领用退库审核")
    private int typeId;

    private Boolean open;

    private String remark;

}
