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
@Entity
@Table(name = "picture_vector")
@Accessors(chain = true)
public class PictureVector extends BaseEntity {

    /**
     * 分组
     */
    @Column(nullable = false)
    private String groupName;

    /**
     * 对应数据ID
     */
    @Column(nullable = false, unique = true)
    private String dataId;

    /**
     * 词向量
     */
    @Column(nullable = false, columnDefinition = "text")
    private String vectorValue;

}
