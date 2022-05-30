package com.iotinall.canteen.constants;

/**
 * 终端常量
 *
 * @author loki
 * @date 2020/10/29 19:16
 */
public class TerminalConstants {
    /**
     * 人脸识别终端和卡片类设备类型 consume-消费类  pass-通行类 attendance-考勤类 collect-采集类 deliver-发卡类 consume_pass-消费+通行 other -其他
     */
    public interface TERMINAL_USETYPE {
        String TERMINAL_USETYPE_CONSUME = "consume";
        String TERMINAL_USETYPE_PASS = "pass";
        String TERMINAL_USETYPE_ATTENDANCE = "attendance";
        String TERMINAL_USETYPE_COLLECT = "collect";
        String TERMINAL_USETYPE_DELIVER = "deliver";
        String TERMINAL_USETYPE_CONSUME_PASS = "consume_pass";
        String TERMINAL_USETYPE_OTHER = "other";
    }

    /**
     * 监控设备类型 kitchen-名厨亮灶 canteen_real-餐厅实况 supervision-监管  other -其他
     */
    public interface MONITOR_TYPE {
        String MONITOR_TYPE_KITCHEN = "kitchen";
        String MONITOR_TYPE_canteen_real = "canteen_real";
        String MONITOR_TYPE_SUPERVISION = "supervision";
        String MONITOR_TYPE_OTHER = "other";
    }

    /**
     * 设备状态 0-待激活 1-正常 2-异常
     */
    public interface STATUS {
        Integer STATUS_INACTIVATED = 0;
        Integer STATUS_NORMAL = 1;
        Integer STATUS_ERR = 2;
    }

    /**
     * 设备类型 face_terminal-人脸识别终端 card-卡片相关设备 monitor-监控设备
     */
    public interface TERMINAL_TYPE {
        String TERMINAL_TYPE_FACE_RECOGNITION = "face_recognition";
        String TERMINAL_TYPE_CARD = "card";
        String TERMINAL_TYPE_MONITOR = "monitor";
    }

    /**
     * 人脸终端相关
     */
    public static final Integer FACE_TERMINAL_QRY_TYPE_EMP_NO = 27;
    public static final Integer FACE_TERMINAL_QRY_TYPE_EMP_NAME = 55;
    public static final Long FACE_TERMINAL_QRY_TYPE_EMP_IDENTITY_NO = 58L;

    /**
     * 设备类型
     */
    public static final String EQU_TYPE_FACE = "face";

    /**
     * 设备类型 0-闸机 1-人脸识别终端 2-人脸录入终端 3-读卡器 4-发卡器
     */
    public static final int EQU_TYPE_GATE = 0;
    public static final int EQU_TYPE_FACE_RECOGNITION = 1;
    public static final int EQU_TYPE_FACE_INPUT = 2;
    public static final int EQU_TYPE_CARD_READ = 3;
    public static final int EQU_TYPE_CARD_OPEN = 4;

    /**
     * 设备状态 0-待激活 1-正常 2-异常 3-维修
     */
    public static final int EQU_STATUS_WAIT_ACTIVE = 0;
    public static final int EQU_STATUS_NORMAL = 1;
    public static final int EQU_STATUS_ERROR = 2;
    public static final int EQU_STATUS_REPAIR = 3;

    /**
     * 同步任务类型
     * 1-后厨人员
     */
    public static final int SYNC_TARGET_TYPE_KITCHEN_EMPLOYEE = 1;

    /**
     * 人脸终端证件类型
     * 0:身份证
     * 1:IC 卡
     * 99:其他
     */
    public static final Integer IDENTIFICATION_TYPE_ID_NUMBER = 0;
    public static final Integer IDENTIFICATION_TYPE_IC_CARD = 1;
    public static final Integer IDENTIFICATION_TYPE_OTHER = 99;
}
