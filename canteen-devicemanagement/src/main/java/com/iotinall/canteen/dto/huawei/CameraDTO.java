package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;

/**
 * 华为摄像头DTO
 *
 * @author loki
 * @date 2021/7/9 11:34
 **/
@Data
public class CameraDTO implements Serializable {
    private String code;
    private String name;
}
