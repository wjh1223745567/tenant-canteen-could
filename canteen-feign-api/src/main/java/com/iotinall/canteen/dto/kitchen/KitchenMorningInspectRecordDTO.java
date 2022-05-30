package com.iotinall.canteen.dto.kitchen;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 晨检记录
 *
 * @date 2021/03/12 17:22
 */
@Data
public class KitchenMorningInspectRecordDTO implements Serializable {
    private Long id;

    private Long empId;

    private String empName;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

    /**
     * 检查状态
     */
    private Integer state;

    /**
     * 每日检查的图片。由每日考勤打卡时获取第一张照片
     */
    private String img;

    /**
     * 检查详情
     */
    private String details;

    /**
     * 备注
     */
    private String comments;

    /**
     * 体温
     */
    private Float temperature;
}
