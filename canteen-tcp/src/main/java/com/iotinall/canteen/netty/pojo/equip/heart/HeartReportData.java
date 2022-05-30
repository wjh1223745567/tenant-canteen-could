package com.iotinall.canteen.netty.pojo.equip.heart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 心跳
 *
 * @author loki
 * @date 2020/06/02 16:25
 */
@Data
public class HeartReportData {


    @JSONField(name = "Time", ordinal = 1)
    private String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{\r\n" +
                "\"Time\": \"" + time + "\"\r\n" +
                "}\r\n";
    }


}
