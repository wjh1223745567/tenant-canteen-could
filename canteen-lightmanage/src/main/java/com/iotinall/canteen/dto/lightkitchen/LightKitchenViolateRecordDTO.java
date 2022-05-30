package com.iotinall.canteen.dto.lightkitchen;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 名厨亮灶违规记录
 *
 * @author loki
 * @date 10/10/2019 20:46
 */
@Data
public class LightKitchenViolateRecordDTO implements Serializable {
    private Long id;

    /**
     * 违规图片
     */
    @JsonSerialize(using = ImgPair.ImgSerializer.class)

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
}
