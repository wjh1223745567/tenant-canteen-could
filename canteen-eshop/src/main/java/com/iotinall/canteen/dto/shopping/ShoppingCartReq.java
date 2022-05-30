package com.iotinall.canteen.dto.shopping;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加购物车
 * @author WJH
 * @date 2019/11/2311:35
 */
@Data
public class ShoppingCartReq {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotNull(message = "数量不能为空")
    @Min(value = 0, message = "数量不能为负数")
    private Integer count;

}
