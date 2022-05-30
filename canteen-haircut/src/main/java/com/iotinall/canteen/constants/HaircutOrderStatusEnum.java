package com.iotinall.canteen.constants;

import lombok.Getter;

/**
 * @description:理发订单状态
 * @author: JoeLau
 * @time: 2021年06月25日 14:49:49
 */

public enum HaircutOrderStatusEnum {
    NOT_SERVED(0, "待服务"),
    IS_CANCEL(1, "已取消"),
    IS_DURING(2, "服务中"),
    IS_FINISHED(3, "已完成"),
    IS_PASSED(4, "已过号");

    @Getter
    int code;
    @Getter
    String statusMsg;

    HaircutOrderStatusEnum(int code, String statusMsg) {
        this.code = code;
        this.statusMsg = statusMsg;
    }
}
