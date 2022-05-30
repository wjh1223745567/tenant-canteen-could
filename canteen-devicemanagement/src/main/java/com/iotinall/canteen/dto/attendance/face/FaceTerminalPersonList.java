package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author loki
 * @date 2020/06/11 20:35
 */
@Data
public class FaceTerminalPersonList implements Serializable {
    @JSONField(name = "Num", ordinal = 1)
    private Integer num;

    @JSONField(name = "PersonInfoList", ordinal = 2)
    private List<FaceTerminalPersonDetail> personInfoList;
}
