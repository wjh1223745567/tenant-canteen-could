package com.iotinall.canteen.dto.goodstype;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 商品类型树
 */
@Data
@Accessors(chain = true)
public class GoodsTypeTreeDTO {
    private Long id;
    private String name;
    private String code;
    private Long parentId;
    private Integer goodsCount;
    private List<GoodsTypeTreeDTO> children;
}
