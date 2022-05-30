package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 光盘行动违规图片记录
 *
 * @author loki
 * @date 10/10/2019 20:46
 */
@Data
@Entity
@Table(name = "empty_plate_img_record")
@EqualsAndHashCode(callSuper = false)
public class EmptyPlateImgRecord extends BaseEntity {
    /**
     * 设备sn
     */
    private String deviceSn;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 原图路径
     */
    private String originalImgUrl;

    /**
     * 描绘违规人员框图片路径
     */
    private String frameImgUrl;

    /**
     * 坐标
     */
    @Column(columnDefinition = "longtext")
    private String coordinate;

    /**
     * 0/null-未分析
     * 1-分析成功
     * 2-分析失败
     * 3-违规照片不存在
     * 4-违规照片中未包含违规人员信息
     */
    private Integer analysis;

    /**
     * 对应餐厅ID
     */
    private Long tenantOrgId;

    /**
     * 对应餐次类型
     */
    private Integer mealType;
}
