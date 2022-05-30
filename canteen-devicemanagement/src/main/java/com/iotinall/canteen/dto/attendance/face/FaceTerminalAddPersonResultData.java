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
public class FaceTerminalAddPersonResultData implements Serializable {
    @JSONField(name = "Num", ordinal = 1)
    private Long num;

    @JSONField(name = "PersonList", ordinal = 2)
    private List<FaceTerminalAddPersonResultDetail> personList;
}
