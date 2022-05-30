package com.iotinall.canteen.dto.sharingscale;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 指标说明
 */
@Data
@Accessors(chain = true)
public class StandardRemarkDto {

    private String name;

    private List<Double> scaleValue;

    private List<StandardSubRemarkDto>  remarks;

}
