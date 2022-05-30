package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 获取版本号请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
@Accessors(chain = true)
public class VersionReq implements Serializable {
    private Integer type;

    @JsonProperty(value = "startdate")
    private String startDate;
}
