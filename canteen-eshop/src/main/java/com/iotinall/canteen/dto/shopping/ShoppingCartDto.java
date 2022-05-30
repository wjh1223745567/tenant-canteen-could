package com.iotinall.canteen.dto.shopping;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WJH
 * @date 2019/11/2311:31
 */
@Data
@Accessors(chain = true)
public class ShoppingCartDto {

    private Long id;

    private List<ShoppingCartProductDto> products;

    private LocalDateTime createTime;
}
