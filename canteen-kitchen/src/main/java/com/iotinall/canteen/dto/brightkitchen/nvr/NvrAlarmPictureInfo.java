package com.iotinall.canteen.dto.brightkitchen.nvr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 人脸终端请求返回体
 */
@Data
public class NvrAlarmPictureInfo implements Serializable {
    @JSONField(name = "Index")
    private String index;

    @JSONField(name = "Format")
    private String format;

    @JSONField(name = "Width")
    private String width;

    @JSONField(name = "Height")
    private String height;

    @JSONField(name = "CaptureTime")
    private String captureTime;

    @JSONField(name = "Size")
    private String size;

    @JSONField(name = "Data")
    private String data;
}
