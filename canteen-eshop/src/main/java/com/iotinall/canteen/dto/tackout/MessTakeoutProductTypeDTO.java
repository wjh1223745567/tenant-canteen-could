package com.iotinall.canteen.dto.tackout;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MessTakeoutProductTypeDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String remark;
    private List<MessTakeoutProductTypeDTO> children;
}
