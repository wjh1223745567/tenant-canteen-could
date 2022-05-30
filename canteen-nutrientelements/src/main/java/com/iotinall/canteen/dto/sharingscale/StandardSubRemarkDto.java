package com.iotinall.canteen.dto.sharingscale;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StandardSubRemarkDto {

    private String name;

    private String remark;

}
