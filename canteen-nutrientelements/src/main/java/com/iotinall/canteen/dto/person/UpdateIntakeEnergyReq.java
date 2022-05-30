package com.iotinall.canteen.dto.person;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 更新用户计划摄入能量
 *
 * @author loki
 * @date 2020/12/08 14:18
 */
@Data
@ApiModel(value = "更新用户计划摄入能量")
public class UpdateIntakeEnergyReq implements Serializable {
    private BigDecimal intake;
}
