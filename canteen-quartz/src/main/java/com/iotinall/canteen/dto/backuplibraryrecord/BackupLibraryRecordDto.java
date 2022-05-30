package com.iotinall.canteen.dto.backuplibraryrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BackupLibraryRecordDto {

    private Long id;

    /**
     * 备份的数据库名称，多个逗号分隔
     */
    private String libraryName;

    /**
     * 备份文件位置
     */
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String file;

    /**
     * 大小
     */
    private String size;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
