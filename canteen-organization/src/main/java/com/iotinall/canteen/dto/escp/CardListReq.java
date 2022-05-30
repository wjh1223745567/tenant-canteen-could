package com.iotinall.canteen.dto.escp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取部门列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
@Accessors(chain = true)
public class CardListReq {
    /**
     * 一次已请求获取数据数量
     */
    private Integer count;

    /**
     * 版本号
     */
    private Long ver;
}
