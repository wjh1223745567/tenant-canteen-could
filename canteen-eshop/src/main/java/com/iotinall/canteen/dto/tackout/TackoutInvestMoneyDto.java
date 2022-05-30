package com.iotinall.canteen.dto.tackout;

import com.iotinall.canteen.constants.TackOutPayType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author WJH
 * @date 2019/11/2511:38
 */
@Data
@Accessors(chain = true)
public class TackoutInvestMoneyDto<T>{

    private T data;

    private TackOutPayType tackOutPayType;

    private Long orderId;

}
