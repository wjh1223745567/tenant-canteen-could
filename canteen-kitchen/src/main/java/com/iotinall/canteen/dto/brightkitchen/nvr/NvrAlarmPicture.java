package com.iotinall.canteen.dto.brightkitchen.nvr;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * nvr 上报图像
 */
@Data
public class NvrAlarmPicture implements Serializable {
    @JSONField(name = "ImageNum")
    private String imageNum;

    @JSONField(name = "ImageInfoList")
    private List<NvrAlarmPictureInfo> imageInfoList;
}
