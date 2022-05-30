package com.iotinall.canteen.dto.sharingscale;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 数据
 */

@Data
public class SharingScaleMeasureDto {

    private Integer type;

    private BigDecimal value;

}
