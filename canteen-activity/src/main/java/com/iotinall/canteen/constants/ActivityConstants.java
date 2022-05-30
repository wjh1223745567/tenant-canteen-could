package com.iotinall.canteen.constants;

/**
 * @author joelau
 * @date 2021/06/01 17:16
 */
public class ActivityConstants {
    //投票活动
    public static final Integer VOTING_ACTIVITY = 0;

    //问卷调查
    public static final Integer QUESTIONNAIRE_SURVEY = 1;

    //单选题
    public static final Integer SINGLE_CHOICE = 0;

    //多选题
    public static final Integer MULTIPLE_CHOICE = 1;

    //问答题
    public static final int SHORT_ANSWER = 2;


    //活动进行中
    public static final int PROCESSING = 0;

    //活动已参与
    public static final int PARTICIPATED = 1;

    //活动已过期
    public static final int EXPIRED = 2;

    //活动还未到
    public static final int NOT_ARRIVED = 3;
}
