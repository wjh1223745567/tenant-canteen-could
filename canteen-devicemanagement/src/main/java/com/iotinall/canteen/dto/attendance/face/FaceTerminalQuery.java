package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 人脸终端查询条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FaceTerminalQuery extends FaceTerminalPageResult implements Serializable {
    @JSONField(name = "Num", ordinal = 1)
    private Integer Num;

    @JSONField(name = "QueryInfos", ordinal = 2)
    private List<FaceTerminalQueryDetail> queryDetailList;
}
