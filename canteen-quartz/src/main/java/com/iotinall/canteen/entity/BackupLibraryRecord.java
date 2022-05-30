package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "backup_library_record")
public class BackupLibraryRecord extends BaseEntity {

    /**
     * 备份的数据库名称，多个逗号分隔
     */
    @Column(columnDefinition = "text")
    private String libraryName;

    /**
     * 备份文件位置
     */
    @Column(length = 1000)
    private String file;

    /**
     * 大小
     */
    private String size;

}
