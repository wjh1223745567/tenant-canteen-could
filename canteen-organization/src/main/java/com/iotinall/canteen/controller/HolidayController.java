package com.iotinall.canteen.controller;

import com.iotinall.canteen.dto.holiday.FeignHolidayDTO;
import com.iotinall.canteen.service.HolidayService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 组织员工 Controller
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@RestController
@RequestMapping("holiday")
public class HolidayController {
    @Resource
    private HolidayService holidayService;

    /**
     * 判断是否为节假日
     *
     * @author loki
     * @date 2021/7/29 15:03
     **/
    @ApiOperation(value = "判断是否为节假日", notes = "判断是否为节假日")
    @GetMapping("is-holiday")
    public Boolean isHoliday(@RequestParam("date") LocalDate date) {
        return holidayService.isHoliday(date);
    }

    /**
     * 获取节假日信息
     *
     * @author loki
     * @date 2021/7/29 14:50
     **/
    @GetMapping(value = "/getByDate")
    FeignHolidayDTO getByDate(@RequestParam("date") LocalDate date) {
        return this.holidayService.getByDate(date);
    }
}