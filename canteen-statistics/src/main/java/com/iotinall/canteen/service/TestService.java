package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
public class TestService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Scheduled(fixedDelay = 6000)
    public void test(){
        String sql = "select count(0) from (\n" +
                "select * from `tenant-canteen-ct1`.`mess_product` as ct1m union all\n" +
                "select * from `tenant-canteen-ct2`.`mess_product` as ct2m) as ccta";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        log.info(JSON.toJSONString(map));
    }

}
