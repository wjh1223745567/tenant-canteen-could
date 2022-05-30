package com.iotinall.canteen.dto.diet;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 饮食记录
 *
 * @author loki
 * @date 2020/04/10 19:38
 */
@Data
public class DietRecordResp implements Serializable {
    /**
     * 能量差值
     */
    NutritionDiffDTO nutrition;

    /**
     * 早餐饮食记录
     */
    List<DietRecordDTO> breakfast;

    /**
     * 午餐饮食记录
     */
    List<DietRecordDTO> lunch;

    /**
     * 晚餐饮食记录
     */
    List<DietRecordDTO> dinner;

    /**
     * 加餐饮食记录
     */
    List<DietRecordDTO> snack;
}
