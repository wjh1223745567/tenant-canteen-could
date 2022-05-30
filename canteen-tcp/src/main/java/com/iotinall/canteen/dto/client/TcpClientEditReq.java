package com.iotinall.canteen.dto.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * tcp client 添加请求参数
 *
 * @author loki
 * @date 2021/6/25 11:20
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TcpClientEditReq extends TcpClientAddReq {
    private Long id;
}
