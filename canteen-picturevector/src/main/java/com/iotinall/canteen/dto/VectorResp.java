package com.iotinall.canteen.dto;

import lombok.Data;

@Data
public class VectorResp {

    /**
     * 1请求正常
     */
    private Integer result;

    /**
     * 错误提示信息
     */
    private String info;

    /**
     * 图片向量
     */
    private Double[] vector;

}
