package com.iotinall.canteen.dto.apptackout;

import com.iotinall.canteen.constants.TackOutPayType;
import com.iotinall.canteen.constants.TakeoutStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WJH
 * @date 2019/11/2214:19
 */
@Data
@Accessors(chain = true)
public class OutSourcingDto {

    private Long id;

    private String empName;

    private String phone;

    private String orderNumber;

    /**
     * 购买商品
     */
    private List<OutSourcingProductDto> products;

    /**
     * 取货码
     */
    private String serialNumber;

    private TackOutPayType tackOutPayType;

    private Integer allCount;

    private BigDecimal payAmount;

    private TakeoutStatus takeoutStatus;

    private LocalDateTime payTime;

    private LocalDateTime createTime;

}
