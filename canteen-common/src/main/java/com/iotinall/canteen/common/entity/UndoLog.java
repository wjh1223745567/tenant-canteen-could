package com.iotinall.canteen.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 自动生成seata log表
 */
@Data
@Entity
@Table(name = "undo_log")
@Accessors(chain = true)
public class UndoLog {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long branchId;

    @Column(nullable = false, length = 100)
    private String xid;

    @Column(nullable = false, length = 128)
    private String context;

    @Column(nullable = false, columnDefinition = "longblob")
    private String rollbackInfo;

    @Column(nullable = false)
    private Integer logStatus;

    @Column(nullable = false)
    private LocalDateTime logCreated;

    @Column(nullable = false)
    private LocalDateTime logModified;

    @Column(length = 100)
    private String ext;
}
