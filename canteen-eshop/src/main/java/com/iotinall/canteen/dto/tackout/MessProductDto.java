package com.iotinall.canteen.dto.tackout;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author WJH
 * @date 2019/11/2314:58
 */
@Data
@Accessors(chain = true)
public class MessProductDto {

    private Long id;

    private Long tackOutId;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String image;

    private String name;

    private BigDecimal price;

    private String tackOutTypeName;

    private String remark;

    private String unit;

    private String specificationModel;

    private Integer limitCount;

    /**
     * 库存
     */
    private Integer stock;

    private BigDecimal star;

    private Integer numberOfShoppingCarts;

}
