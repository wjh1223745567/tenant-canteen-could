package com.iotinall.canteen.dto.live;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author bingo * @date 1/10/2020 20:07
 */
@Data
@ApiModel(description = "餐位信息")
public class SeatInfoDTO {
    private Integer capacity;
    private Integer dinerCount;
}
