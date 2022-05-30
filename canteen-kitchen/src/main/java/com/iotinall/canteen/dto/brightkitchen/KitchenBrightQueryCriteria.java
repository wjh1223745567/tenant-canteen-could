package com.iotinall.canteen.dto.brightkitchen;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bingo
 * @date 1/4/2020 18:40
 */
@Data
public class KitchenBrightQueryCriteria {
    private Integer type;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String keyword;
}
