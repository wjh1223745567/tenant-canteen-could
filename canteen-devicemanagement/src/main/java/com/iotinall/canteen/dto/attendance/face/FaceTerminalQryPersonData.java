package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author loki
 * @date 2020/06/11 20:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FaceTerminalQryPersonData extends FacePageResult implements Serializable {
    @JSONField(name = "PersonList", ordinal = 5)
    private FaceTerminalPersonList personList;
}
