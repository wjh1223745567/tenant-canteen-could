package com.iotinall.canteen.dto.operationlog;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OperationLogDto {

    /**
     * 实体名称
     */
    private String className;

    private String data;

    /**
     * 操作类型
     */
    private Integer type;

    private String typeName;

    private LocalDateTime createTime;

}
