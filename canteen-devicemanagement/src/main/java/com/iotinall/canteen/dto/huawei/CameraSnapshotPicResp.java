package com.iotinall.canteen.dto.huawei;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 摄像机抓怕图片返回
 *
 * @author loki
 * @date 2021/7/1 11:06
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CameraSnapshotPicResp extends HuaweiApiResp {
    private CameraSnapshotPic snapshotInfoList;
}
