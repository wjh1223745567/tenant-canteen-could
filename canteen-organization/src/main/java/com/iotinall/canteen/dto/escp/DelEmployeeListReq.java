package com.iotinall.canteen.dto.escp;

import lombok.Data;

/**
 * 获取注销人员列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class DelEmployeeListReq {
    /**
     * 一次已请求获取数据数量
     */
    private Integer count;

    /**
     * 版本号
     */
    private Long ver;
}
