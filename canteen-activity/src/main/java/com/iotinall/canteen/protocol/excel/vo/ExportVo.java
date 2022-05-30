package com.iotinall.canteen.protocol.excel.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loki
 * @date 2019-10-15 20:12
 **/
@Data
public class ExportVo implements Serializable {
    /**
     * 导出类型
     */
    private String exportType;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 缓存key
     */
    private String cacheKey;

    /**
     * 分区索引
     */
    private Integer partitionIndex;

    /**
     * 查询条件
     */
    private String query;

    /**
     * 导出文件名称
     */
    private String fileName;

    /**
     * 分页
     */
    private Integer pageNumber;

    /**
     * 每页大小
     */
    private Integer pageSize;
}
