package com.iotinall.canteen.dto.sourcing;

import lombok.Data;

import java.time.LocalDate;

/**
 * 搜索条件
 * @author WJH
 * @date 2019/11/2214:31
 */
@Data
public class OutSourcingCondition{

    private LocalDate createTime;

    private Integer takeoutStatus;

    private String empName;
}
