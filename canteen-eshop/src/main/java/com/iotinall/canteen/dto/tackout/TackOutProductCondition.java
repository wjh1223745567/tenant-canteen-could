package com.iotinall.canteen.dto.tackout;

import lombok.Data;

/**
 * @author WJH
 * @date 2019/11/2615:55
 */
@Data
public class TackOutProductCondition {
    /**
     * 商品类型
     */
    private Long typeId;

    /**
     * 商品名称
     */
    private String name;

    private Boolean state;

    private String keyWords;
}
