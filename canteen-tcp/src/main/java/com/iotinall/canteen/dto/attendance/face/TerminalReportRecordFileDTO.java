package com.iotinall.canteen.dto.attendance.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 卡信息列表
 *
 * @author loki
 * @date 2020/06/02 17:48
 */
@Data
public class TerminalReportRecordFileDTO {
    /**
     * 文件名称，长度范围[1, 16]。
     */
    @JSONField(name = "Name")
    private String name;

    /**
     * Data 字段：图片转为base64 后的字符串长度，
     * 单位:字节。范围:[0, 1M(1048576)]
     */
    @JSONField(name = "Size")
    private Long size;

    /**
     * 文件 Base64 编码数据，注意数据头中不需要增加base64
     */
    @JSONField(name = "Data")
    private String data;
}
