package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.dto.holiday.FeignHolidayDTO;
import com.iotinall.canteen.entity.HolidayDate;
import com.iotinall.canteen.repository.HolidayDateRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * 节假日逻辑处理类
 *
 * @author loki
 * @date 2021/7/29 14:53
 **/
@Slf4j
@Service("holidayService")
public class HolidayService {
    @Resource
    private HolidayDateRepository holidayDateRepository;

    /**
     * 校验是否为节假日
     *
     * @author loki
     * @date 2021/7/29 14:54
     **/
    public Boolean isHoliday(LocalDate date) {
        HolidayDate holidayDate = holidayDateRepository.findByDate(date);

        return null == holidayDate ? false : holidayDate.getHoliday();
    }

    /**
     * 获取节假日信息
     *
     * @author loki
     * @date 2021/7/30 15:18
     **/
    public FeignHolidayDTO getByDate(LocalDate date) {
        HolidayDate holidayDate = holidayDateRepository.findByDate(date);
        if (null != holidayDate) {
            FeignHolidayDTO feignHolidayDTO = new FeignHolidayDTO();
            BeanUtils.copyProperties(holidayDate, feignHolidayDTO);
            return feignHolidayDTO;
        }

        return null;
    }

    /**
     * 每月更新一次
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 10 0 1 * ?")
    public void holidayInfo() {
        Integer month = LocalDate.now().getMonthValue();
        Integer year = LocalDate.now().getYear();
        if (month > 6) {
            year++;
        }
        log.info("获取最新假日信息：{}", year);
        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://timor.tech/api/holiday/year/" + year + "/")
                .build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                Map<String, String> map = JSON.parseObject(EntityUtils.toString(entity), new TypeReference<Map<String, String>>() {
                });
                if (StringUtils.isNotBlank(map.get("holiday"))) {
                    Map<String, HolidayDate> holidayDateMap = JSON.parseObject(map.get("holiday"), new TypeReference<Map<String, HolidayDate>>() {
                    });
                    for (Map.Entry<String, HolidayDate> stringHolidayDateEntry : holidayDateMap.entrySet()) {
                        HolidayDate holidayDate = stringHolidayDateEntry.getValue();
                        this.holidayDateRepository.save(holidayDate);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
