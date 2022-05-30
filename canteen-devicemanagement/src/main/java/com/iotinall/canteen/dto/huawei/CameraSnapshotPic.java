package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 抓拍图片
 *
 * @author loki
 * @date 2021/7/1 11:00
 **/
@Data
public class CameraSnapshotPic implements Serializable {
    private Integer total;

    private IndexRange indexRange;

    private List<CameraSnapshotPic> snapshotInfos;
}
