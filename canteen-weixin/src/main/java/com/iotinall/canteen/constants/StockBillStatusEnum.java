package com.iotinall.canteen.constants;

import lombok.Getter;

/**
 * 单据类型枚举
 *
 * @author loki
 * @date 2020/09/14 14:47
 */
@Getter
public enum StockBillStatusEnum {
    /**
     * 单据状态
     */
    STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0(StockConstants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0, "待审核"),
    STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1(StockConstants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1, "待审核"),
    STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE(StockConstants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE, "待验收"),
    STOCK_IN_OUT_ORDER_STATUS_FINISH(StockConstants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH, "已完成"),
    STOCK_IN_OUT_ORDER_STATUS_REFUSE(StockConstants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_REFUSE, "已拒绝"),
    STOCK_IN_OUT_ORDER_STATUS_CANCEL(StockConstants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_CANCEL, "已取消");

    private int code;
    private String value;

    StockBillStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 获取单据名称
     *
     * @author loki
     * @date 2020/09/14 14:55
     */
    public static String getBillStatusName(int code) {

        for (StockBillStatusEnum typeEnum : StockBillStatusEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum.getValue();
            }
        }

        return "";
    }
}
