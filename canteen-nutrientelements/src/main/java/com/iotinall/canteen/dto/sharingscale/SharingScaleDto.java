package com.iotinall.canteen.dto.sharingscale;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class SharingScaleDto {

    private String sn;

    private BigDecimal weight;

    private BigDecimal height;

    private Integer gender;

    private Integer age;

    private Integer isAthlete;

    private List<SharingScaleMeasureDto> measurement;

}
