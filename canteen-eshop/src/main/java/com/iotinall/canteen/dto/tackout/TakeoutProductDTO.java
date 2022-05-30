package com.iotinall.canteen.dto.tackout;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author WJH
 * @date 2019/11/2615:55
 */
@Data
@Accessors(chain = true)
public class TakeoutProductDTO {

    private Long id;

    private String name;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;

    private String typeName;

    private Integer count;

    private BigDecimal price;

    private Integer salesVolume;

    private String unit;

    private Long typeId;

    private String specificationModel;

    private String serialCode;

    private Integer limitCount;

    private Boolean state;

    private Boolean top;

    private String remark;
}
