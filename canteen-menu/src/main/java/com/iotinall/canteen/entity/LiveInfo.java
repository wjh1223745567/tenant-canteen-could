package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.constants.AreaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 餐厅实况
 *
 * @author bingo
 * @date 1/3/2020 16:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "live_info")
public class LiveInfo extends BaseEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 对应摄像头ID
     */
    @JoinColumn(name = "device_id", nullable = false)
    private Long deviceId;

    /**
     * 默认图片
     */
    @Column(length = 350)
    private String defaultImg;

    /**
     * 检测时间
     */
    private LocalDateTime detectTime;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 类型
     */
    @Enumerated
    private AreaType areaType;

    /**
     * 安装位置
     */
    private String areaName;

    /**
     * 租户组织ID
     */
    private Long tenantOrgId;
}
