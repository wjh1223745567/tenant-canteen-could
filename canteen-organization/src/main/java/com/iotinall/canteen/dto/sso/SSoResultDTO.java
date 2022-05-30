package com.iotinall.canteen.dto.sso;

import lombok.Data;

@Data
public class SSoResultDTO<T> {

    private Integer code;

    private T data;

    private String msg;

    private Long timestamp;

}
