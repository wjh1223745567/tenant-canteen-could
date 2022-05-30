package com.iotinall.canteen.netty.pojo.equip.report;

import com.alibaba.fastjson.annotation.JSONField;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 推送记录返回对象
 *
 * @author loki
 * @date 2020/06/02 20:28
 */
@Data
public class ReportBack {
    @JSONField(name = "Response")
    private ResponseDetail response;

    public ReportBack(Boolean success, Long id) {
        if (success) {
            this.response = new ResponseDetail().setData(new RecordData(id))
                    .setStatusCode(0)
                    .setStatusString("Succeed");
        } else {
            this.response = new ResponseDetail().setData(new RecordData(id))
                    .setStatusCode(1)
                    .setStatusString("Fail");
        }
    }

    @Data
    @Accessors(chain = true)
    public class ResponseDetail {
        @JSONField(name = "ResponseURL", ordinal = 1)
        private String responseURL = "/LAPI/V1.0/PACS/Controller/Event/Notifications";

        @JSONField(name = "StatusCode", ordinal = 2)
        private Integer statusCode;

        @JSONField(name = "StatusString", ordinal = 3)
        private String statusString;

        @JSONField(name = "Data", ordinal = 4)
        private RecordData data;
    }

    @Data
    public class RecordData {
        @JSONField(name = "RecordID", ordinal = 1)
        private Long recordID;

        @JSONField(name = "Time", ordinal = 2)
        private String time;

        RecordData(Long recordId) {
            this.recordID = recordId;
            this.time = LocalDateTimeUtil.localDatetime2Str(LocalDateTime.now());
        }
    }
}
