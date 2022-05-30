package com.iotinall.canteen.protocol.excel.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author loki
 * @date 2019/10/15 14:08
 */
@Data
public class ExcelExportModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分片数量
     */
    private int partitionCount;
    /**
     * 导出数据总量
     */
    private int dataTotalCount;
    /**
     * 缓存key
     */
    private String cacheKey;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 是否已经导出
     */
    private Boolean isExported = Boolean.FALSE;

    /**
     * 导出文件Id
     */
    private String fileId;
}
