package com.iotinall.canteen.constant;

/**
 * TCP常量
 *
 * @author loki
 * @date 2021/6/25 14:26
 **/
public class TcpConstants {
    /**
     * TCP CLIENT 客户端类型
     */
    public final static String CLIENT_TYPE_COMMENT = "comment";
    public final static String CLIENT_TYPE_BIG_SCREEN = "big_screen";
    public final static String CLIENT_TYPE_DINER_NUMBER = "diner_number";
    public final static String CLIENT_TYPE_EMPTY_PLATE = "empty_plate";

    /**
     * 状态 0-离线 1-在线
     */
    public final static int CLIENT_STATUS_OFFLINE = 0;
    public final static int CLIENT_STATUS_ONLINE = 1;
}
