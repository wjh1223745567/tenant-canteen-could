package com.iotinall.canteen.constants;

/**
 * @author bingo
 * @date 11/28/2019 16:02
 */
public class RedisConstants {

    /**
     * 所有redis中key 过期事件都以EVENT开头
     */
    public static final String REDIS_EVENT = "EVENT:";

    /**
     * 人员变动，异步更新设备数据
     */

    public static final int SYNC_TYPE_DEL = 2;
}
