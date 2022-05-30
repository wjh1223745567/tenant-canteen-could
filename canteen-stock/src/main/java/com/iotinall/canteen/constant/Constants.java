package com.iotinall.canteen.constant;

/**
 * 库存模块常量类
 */
public abstract class Constants {
    /**
     * 平台类型
     */
    public final static String PLAT_MINI = "0";
    public final static String PLAT_WEB = "1";

    /**
     * 预警类型
     * 1-上限预警
     * 2-下限预警
     * 3-保质期预警
     */
    public final static int WARNING_TYPE_UPPER = 1;
    public final static int WARNING_TYPE_LIMIT = 2;
    public final static int WARNING_TYPE_SHELF_LIFE = 3;

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
     * 待办操作类型
     * approval-审核
     * handle-经办
     */
    public final static String TASK_DEFINE_AUDIT = "audit";
    public final static String TASK_DEFINE_ACCEPTANCE = "acceptance";

    /**
     * 操作类型
     * apply-申请
     * approval-审核
     * acceptance-验收
     * cancel - 取消
     */
    public interface TASK_DEFINE {
        String APPLY = "apply";
        String AUDIT = TASK_DEFINE_AUDIT;
        String ACCEPTANCE = TASK_DEFINE_ACCEPTANCE;
        String END = "end";
    }


    /**
     * 操作类型
     * apply-申请
     * audit-审核
     * acceptance-验收
     * cancel - 取消
     */
    public interface OPT_TYPE {
        String APPLY = "apply";
        String AUDIT = "audit";
        String ACCEPTANCE = "acceptance";
        String CANCEL = "cancel";
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


    /**
     * 状态
     * 1-待办
     * 2-已办
     */
    public final static int TODO_STATUS_INIT = 1;
    public final static int TODO_STATUS_DONE = 2;

    /**
     * 审核状态
     * 1-同意
     * 0-拒绝
     */
    public final static int AGREE = 1;
    public final static int REFUSE = 0;

    /**
     * 初始任务ID
     */
    public final static int INIT_TASK_ID = 0;
    public final static int END_TASK_ID = 99;

    /**
     * 库存统计类型
     */
    public static final int STOCK_STAT_TYPE_LEFT = 1;
    public static final int STOCK_STAT_TYPE_STOCK_IN = 2;
    public static final int STOCK_STAT_TYPE_STOCK_OUT = 3;
}
