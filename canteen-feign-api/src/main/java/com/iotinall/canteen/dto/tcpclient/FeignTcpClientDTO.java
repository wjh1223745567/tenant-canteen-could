package com.iotinall.canteen.dto.tcpclient;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * feign返回对象
 *
 * @author loki
 * @date 2021/7/7 11:48
 **/
@Data
@Accessors(chain = true)
public class FeignTcpClientDTO implements Serializable {
    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 客户端名称
     */
    private String clientName;
}
