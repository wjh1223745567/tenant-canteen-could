package com.iotinall.canteen.constants;

import lombok.Getter;

/**
 * 单据类型枚举
 *
 * @author loki
 * @date 2020/09/14 14:47
 */
@Getter
public enum StockBillTypeEnum {
    /**
     * 单据类型
     */
    STOCK_IN(StockConstants.BILL_TYPE.STOCK_IN, "采购入库"),
    STOCK_IN_BACK(StockConstants.BILL_TYPE.STOCK_IN_BACK, "采购退货"),
    STOCK_OUT(StockConstants.BILL_TYPE.STOCK_OUT, "领用出库"),
    STOCK_OUT_BACK(StockConstants.BILL_TYPE.STOCK_OUT_BACK, "领用退库"),
    STOCK_INVENTORY(StockConstants.BILL_TYPE.STOCK_INVENTORY, "库存盘点");

    private String code;
    private String value;

    StockBillTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 获取单据名称
     *
     * @author loki
     * @date 2020/09/14 14:55
     */
    public static String getBillTypeName(String code) {
        for (StockBillTypeEnum typeEnum : StockBillTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getValue();
            }
        }

        return "";
    }
}
