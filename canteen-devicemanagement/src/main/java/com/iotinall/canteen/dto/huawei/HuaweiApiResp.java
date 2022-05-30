package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;

/**
 * 华为api返回结果
 *
 * @author loki
 * @date 2021/6/28 20:39
 **/
@Data
public class HuaweiApiResp implements Serializable {
    private Integer resultCode;
}
