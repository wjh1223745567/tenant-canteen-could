package com.iotinall.canteen.constant;

/**
 * @author bingo
 * @date 11/28/2019 16:02
 */
public class RedisConstants {

    public static final String REDIS_KEY_SPLIT = ":";
    public static final Integer LOGIN_MOBILE_CODE_EXPIRE = 30 * 60;
    public static final String LOGIN_MOBILE_CODE = "MOBILE_CODE:";
    public static final String CODE = "code";
    public static final String LATEST = "latest";
    public static final Integer LATEST_EXPIRE = 1 * 60;
    public static final String TODAY_TIMES = "times";
    public static final Integer TODAY_MAX_TIMES = 5;

    /**
     * 所有redis中key 过期事件都以EVENT开头
     */
    public static final String REDIS_EVENT = "EVENT:";

    /**
     * 请假审批事件
     */
    public static final String EVENT_LEAVE = REDIS_EVENT + "LEAVE:";
    /**
     *
     */
    public static final String EVENT_BIGSCREEN_WEBSOCKET_CLIENT_HEARTBEAT_EXPIRE = REDIS_EVENT + "HEARTBEAT:";


    /**
     * 人员变动，异步更新设备数据
     */
    public static final int SYNC_TYPE_ADD = 0;
    public static final int SYNC_TYPE_UPDATE = 1;
    public static final int SYNC_TYPE_DEL = 2;
    public static final String EVENT_SYNC_TERMINAL = REDIS_EVENT + "EVENT_SYNC_TERMINAL:";
}
