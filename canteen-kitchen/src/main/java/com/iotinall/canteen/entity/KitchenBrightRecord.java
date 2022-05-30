package com.iotinall.canteen.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 明厨亮灶违规记录
 *
 * @author bingo
 * @date 1/3/2020 17:14
 */
@Data
@Entity
@Table(name = "kitchen_bright_record")
public class KitchenBrightRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String imageFile; // 违规文件
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String originalFile; // 原始图片

    /**
     * 上报类型
     * 1-未穿工作服检测
     * 2-打电话检测
     * 3- 吸烟检测
     * 4-烟火检测
     * 5-移动检测
     * 6-未戴厨师帽检测
     */
    private String type;

    private Long empId;

    private Long cameraId;
    private String kitchenId;
    private String areaName; // 位置名称
    private String version; // 系统检测版本
    private LocalDateTime detectTime; // 检测时间
    @Column(columnDefinition = "text")
    private String alerts; // 检测结果，存储json
    private String ext;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    private String alarmType;
}
