package com.iotinall.canteen.dto.hikvision;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 海康闸机事件订阅
 */
@Data
@Accessors(chain = true)
public class HicEventReq {

    private List<Integer> eventTypes;

    /**
     * 事件回调地址
     */
    private String eventDest;

    /**
     * 订阅类型，0-订阅原始事件，1-联动事件，2-原始事件和联动事件，不填使用默认值0
     */
    private Integer subType;

    /**
     * 	事件等级，0-未配置，1-低，2-中，3-高
     * 此处事件等级是指在事件联动中配置的等级
     * 订阅类型为0时，此参数无效，使用默认值0
     * 在订阅类型为1时，不填使用默认值[1,2,3]
     * 在订阅类型为2时，不填使用默认值[0,1,2,3]
     * 数组大小不超过32，事件等级大小不超过31
     */
    private List<Integer> eventLvl;

}
