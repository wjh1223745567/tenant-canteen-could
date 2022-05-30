package com.iotinall.canteen.dto.apptackout;

import lombok.Data;

import java.time.LocalDate;

/**
 * 搜索条件
 * @author WJH
 * @date 2019/11/2214:31
 */
@Data
public class OutSourcingCondition{

    private String key;

    private Integer sourcingStatus;

    private Long cursor;

    private LocalDate createTime;

}
