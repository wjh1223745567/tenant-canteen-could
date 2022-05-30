package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 名厨亮灶违规记录
 *
 * @author loki
 * @date 10/10/2019 20:46
 */
@Data
@Entity
@Table(name = "light_kitchen_violate_record")
@EqualsAndHashCode(callSuper = false)
public class LightKitchenViolateRecord extends BaseEntity {
    /**
     * 违规图片
     */
    private String imgUrl;

    /**
     * SafetyHelmetAlarm	未佩戴安全帽报警
     * WorkClothesAlarm	未穿戴工作服报警
     * ChefHatAlarm	未佩戴厨师帽报警
     * TelephoningAlarm	打电话报警
     * SmokingAlarm	吸烟报警
     * FastMoving	快速移动
     */
    private String type;

    /**
     * 监控区域
     */
    private String areaName;

    /**
     * 违规时间
     */
    private LocalDateTime detectTime;

    /**
     * 租户ID
     */
    private Long tenantOrgId;
}
