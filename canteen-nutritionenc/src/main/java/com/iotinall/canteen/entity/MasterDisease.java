package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 慢性疾病
 *
 * @author loki
 * @date 2020/12/09 19:41
 */
@Data
@Entity
@Table(name = "master_disease")
@ToString(exclude = "chronicDisease")
@EqualsAndHashCode(exclude = "chronicDisease", callSuper = false)
@JsonIgnoreProperties(value = "chronicDisease")
public class MasterDisease extends BaseEntity implements Serializable {
    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 是否适用男性
     */
    private Boolean male;

    /**
     * 是否适用女性
     */
    private Boolean female;

    /**
     * 对应食谱
     */
    @OneToMany(mappedBy = "masterDisease")
    @OrderBy("seq asc")
    private Set<ChronicDisease> chronicDisease;
}
