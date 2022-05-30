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
public class FaceTerminalAddPersonResultDetail implements Serializable {
    @JSONField(name = "PersonID", ordinal = 1)
    private Long personId;

    @JSONField(name = "FaceNum", ordinal = 2)
    private Long faceNum;

    @JSONField(name = "FaceList", ordinal = 2)
    private List<Face> faceList;


    private class Face {
        @JSONField(name = "FaceID", ordinal = 2)
        private Long faceId;

        @JSONField(name = "ResultCode", ordinal = 2)
        private Long resultCode;
    }
}
