package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;

/**
 * 抓拍图片信息
 *
 * @author loki
 * @date 2021/7/1 11:00
 **/
@Data
public class CameraSnapshotPicInfo implements Serializable {
    /**
     * 摄像机编码
     */
    private String cameraCode;

    /**
     * 抓拍图片时间，格式如yyyyMMddHHmmss
     */
    private String snapTime;

    /**
     * 抓拍类型：
     * 1：智能分析抓拍
     * 2：告警抓拍
     * 4：手动抓拍(包括定时抓拍)说明：异或标记值，例如0x01| 0x02=3表示查询类型包括智能分析抓拍和告警抓拍。
     */
    private Integer snapType;
    /**
     * 抓拍图片ID
     */
    private Integer pictureID;
    /**
     * 文件名
     */
    private String pictureName;
    /**
     * 图片文件大小
     */
    private Integer pictureSize;
    /**
     * 抓拍图片缩略图URL
     */
    private String previewUrl;
    /**
     * 抓拍图片URL
     */
    private String pictureUrl;
    /**
     * 保留字段
     */
    private String reserve;
}
