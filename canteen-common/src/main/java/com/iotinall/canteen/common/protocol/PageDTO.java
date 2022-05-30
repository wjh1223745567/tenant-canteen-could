package com.iotinall.canteen.common.protocol;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 * @author xin-bing
 * @date 10/16/2019 16:07
 */
@Data
public class PageDTO<T> {
    private long total;
    private List<T> content;
}
