package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 晨检记录
 *
 * @date 2021/03/12 17:22
 */
@Data
@Entity
@Table(name = "kitchen_morning_inspect_record")
public class KitchenMorningInspectRecord extends BaseEntity {
    
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
    @Column(columnDefinition = "text")
    private String details;

    /**
     * 备注
     */
    private String comments;

    /**
     * 体温
     */
    private Float temperature;

    @JoinColumn(name = "auditor_id")
    private Long auditor;

    /**
     * 健康码个人档案中 ID
     */
    private Long healthCodeId;

    /**
     * 行程码个人档案中 ID
     */
    private Long itineraryCodeId;
}
