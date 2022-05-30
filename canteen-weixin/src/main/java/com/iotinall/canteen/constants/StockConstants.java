package com.iotinall.canteen.constants;

/**
 * 库存模块常量类
 */
public abstract class StockConstants {

    /**
     * 库存入库出库单状态
     * 0-待审核 未审核
     * 1-待审核 至少存在一次审核
     * 2-待验收
     * 3-已完成
     * 4-已拒绝
     * 5-已取消
     */
    public interface BILL_STATUS {
        int STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0 = 0;
        int STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1 = 1;
        int STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE = 2;
        int STOCK_IN_OUT_ORDER_STATUS_FINISH = 3;
        int STOCK_IN_OUT_ORDER_STATUS_REFUSE = 4;
        int STOCK_IN_OUT_ORDER_STATUS_CANCEL = 5;
    }

    /**
     * 流程配置类型
     * STOCK_IN - 采购入库
     * STOCK_IN_BACK - 采购退货
     * STOCK_OUT - 领用出库
     * STOCK_OUT_BACK - 领用退库
     * STOCK_INVENTORY - 库存盘点
     */
    public interface BILL_TYPE {
        String STOCK_IN = "stock_in";
        String STOCK_IN_BACK = "stock_in_back";
        String STOCK_OUT = "stock_out";
        String STOCK_OUT_BACK = "stock_out_back";
        String STOCK_INVENTORY = "stock_inventory";
    }

}
