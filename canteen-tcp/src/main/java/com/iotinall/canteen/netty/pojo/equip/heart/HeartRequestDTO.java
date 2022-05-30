package com.iotinall.canteen.netty.pojo.equip.heart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 心跳
 *
 * @author loki
 * @date 2020/06/02 16:25
 */
@Data
public class HeartRequestDTO {


    @JSONField(name = "RefId")
    private String strRefId;

    @JSONField(name = "Time")
    private String strTime;

    @JSONField(name = "NextTime")
    private String strNextTime;

    @JSONField(name = "DeviceCode")
    private String strDeviceCode;

    @JSONField(name = "DeviceType")
    private Integer iDeviceType;

    @Override
    public String toString() {
        return "HeartRequestDTO{" +
                "strRefId='" + strRefId + '\'' +
                ", strTime='" + strTime + '\'' +
                ", strNextTime='" + strNextTime + '\'' +
                ", strDeviceCode='" + strDeviceCode + '\'' +
                ", iDeviceType=" + iDeviceType +
                '}';
    }
}
