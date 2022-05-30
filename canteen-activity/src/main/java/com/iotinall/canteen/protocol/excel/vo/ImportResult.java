package com.iotinall.canteen.protocol.excel.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 导入结果
 *
 * @author loki
 * @date 2019-10-18 10:55
 **/
@Data
@Accessors(chain = true)
public class ImportResult implements Serializable {
    /**
     * 缓存key ,下载专用
     */
    private String cacheKey = "";

    /**
     * 错误列表
     */
    private Object errList = "";

    /**
     * 导入成功数量
     */
    private Integer importNums = 0;

    /**
     * 导入失败数量
     */
    private Integer errNums = 0;
}
