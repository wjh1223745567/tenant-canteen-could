package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.holiday.FeignHolidayDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * 节假日
 *
 * @author loki
 * @date 2021/06/03 16:36
 */
@FeignClient(value = "canteen-organization", contextId = "holiday")
public interface FeignHolidayService {
    /**
     * 判断是否是节假日
     *
     * @author loki
     * @date 2021/7/29 14:50
     **/
    @GetMapping(value = "holiday/is-holiday")
    Boolean isHoliday(@RequestParam("date") LocalDate date);

    /**
     * 获取节假日信息
     *
     * @author loki
     * @date 2021/7/29 14:50
     **/
    @GetMapping(value = "holiday/getByDate")
    FeignHolidayDTO getByDate(@RequestParam("date") LocalDate date);
}
