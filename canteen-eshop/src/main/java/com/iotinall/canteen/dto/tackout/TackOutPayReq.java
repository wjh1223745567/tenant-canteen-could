package com.iotinall.canteen.dto.tackout;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author WJH
 * @date 2019/11/2511:21
 */
@Data
public class TackOutPayReq {

    private String openid;

    @NotNull
    private Integer tackOutPayType;

    /**
     * 钱包支付密码
     */
    private String payPassword;

    @NotNull
    private BigDecimal amount;

    /**
     * messProdStock  ID
     */
    @NotEmpty
    private List<TackOutPayProductReq> products;
}
