package com.iotinall.canteen.dto.flwconfig;

import lombok.Data;

import java.util.List;

@Data
public class FlwConfigDTO {
    private Long id;

    private String billType;

    private List<FlwTaskDTO> taskList;
}
