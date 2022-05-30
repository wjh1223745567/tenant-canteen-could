package com.iotinall.canteen.protocol.excel.model;

import lombok.Data;

import java.util.List;

/**
 * @author loki
 * @date 2019/10/15 14:08
 */
@Data
public class ExcelExportPartitionDataModel<T> implements Comparable<ExcelExportPartitionDataModel> {

    /**
     * 分片序号
     */
    private Integer partitionIndex;

    /**
     * 分片导出是否完成
     */
    private Boolean isComplete;

    /**
     * 分片处理的数据集合
     */
    private List<T> dataModelList;

    @Override
    public int compareTo(ExcelExportPartitionDataModel o) {
        return partitionIndex.compareTo(o.getPartitionIndex());
    }
}
