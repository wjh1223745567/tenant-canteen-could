package com.iotinall.canteen.dto.information;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author WJH
 * @date 2019/11/19:57
 */
@Setter
@Getter
public class InformationCondition {

    private Long typeId;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
