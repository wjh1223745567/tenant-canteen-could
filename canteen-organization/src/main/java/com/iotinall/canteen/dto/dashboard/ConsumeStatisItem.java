package com.iotinall.canteen.dto.dashboard;

import java.math.BigDecimal;

public interface ConsumeStatisItem {
    BigDecimal getAmount();

    Long getCount();

    Integer getEatType();

    String getDate();
}
