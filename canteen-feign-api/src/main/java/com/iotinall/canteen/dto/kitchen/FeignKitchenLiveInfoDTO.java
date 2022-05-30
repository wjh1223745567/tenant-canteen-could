package com.iotinall.canteen.dto.kitchen;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后厨实况
 *
 * @author loki
 * @date 2021/7/16 10:21
 **/
@Data
public class FeignKitchenLiveInfoDTO {
    /**
     * 晨检率
     */
    private BigDecimal morningCheckRate = BigDecimal.ZERO;

    /**
     * 违规次数
     */
    private Integer violationNum = 0;
}
