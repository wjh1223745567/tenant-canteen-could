package com.iotinall.canteen.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "userIdentityGenerator")
    @GenericGenerator(name = "userIdentityGenerator", strategy = "com.iotinall.canteen.common.constant.UserIdentityGenerator")
    private Long id;

    /**
     * 创建人
     */
    @Column(updatable = false)
    @CreatedBy
    private Long createId;

    /**
     * 修改人
     */
    @Column
    @LastModifiedBy
    private Long updateId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Column(columnDefinition = "text")
    private String remark;
}
