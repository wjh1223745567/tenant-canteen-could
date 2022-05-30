package com.iotinall.canteen.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FeignSerMoneybagReq {

    private String password;

    private BigDecimal amount;

    private Long tackOutOrderId;

    private LocalDateTime orderCreateTime;

}
