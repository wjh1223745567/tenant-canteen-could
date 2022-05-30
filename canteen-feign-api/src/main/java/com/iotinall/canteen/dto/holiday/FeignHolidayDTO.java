package com.iotinall.canteen.dto.holiday;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 节假日dto
 *
 * @author loki
 * @date 2021/7/30 15:16
 **/
@Data
public class FeignHolidayDTO implements Serializable {
    private LocalDate date;

    /**
     * 是否是假期
     */
    private Boolean holiday;

    /**
     * 假日名称
     */
    private String name;
}
