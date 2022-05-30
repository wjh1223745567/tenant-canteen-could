package com.iotinall.canteen.dto.sourcing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author WJH
 * @date 2019/12/215:53
 */
@Data
@Accessors(chain = true)
public class OutSourcingProductDto {

    private String name;

    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String image;

    private Integer count;
}
