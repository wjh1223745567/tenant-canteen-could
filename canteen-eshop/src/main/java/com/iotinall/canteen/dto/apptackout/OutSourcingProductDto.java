package com.iotinall.canteen.dto.apptackout;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author WJH
 * @date 2019/12/915:11
 */
@Data
@Accessors(chain = true)
public class OutSourcingProductDto {

    private String name;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String image;

    private BigDecimal price;

    private Integer count;
}
