package com.iotinall.canteen.constant;

/**
 * NETTY常量
 *
 * @author loki
 * @date 2020/06/04 14:09
 */
public class NettyConstants {
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONNECTION = "Connection";
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";

    public static final int READ_TIME_OUT = 120;
    public static final int BOSS_GROUP_THREAD_NUM = 1;
    public static final int WORK_GROUP_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;
    public static final int DEAL_BLOCK_SIZE = 256;
    public static final String DEFAULT_MSG_DELIMITER = "<END>";
}
