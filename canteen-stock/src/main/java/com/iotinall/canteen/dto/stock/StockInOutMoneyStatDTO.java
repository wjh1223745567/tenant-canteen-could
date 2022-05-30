package com.iotinall.canteen.dto.stock;

import java.math.BigDecimal;

public interface StockInOutMoneyStatDTO {
    BigDecimal getMoney();

    String getBillType();

    String getDate();
}
