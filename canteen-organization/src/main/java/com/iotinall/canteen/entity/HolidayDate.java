package com.iotinall.canteen.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * 节假日信息
 */
@Data
@Entity
@Table(name = "holiday_date")
public class HolidayDate {

    @Id
    private LocalDate date;

    /**
     * 是否是假期
     */
    @Column(nullable = false)
    private Boolean holiday;

    /**
     * 假日名称
     */
    @Column(length = 50, nullable = false)
    private String name;

}
