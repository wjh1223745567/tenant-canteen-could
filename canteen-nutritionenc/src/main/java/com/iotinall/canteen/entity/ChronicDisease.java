package com.iotinall.canteen.entity;

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
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "chronic_disease")
@ToString(exclude = {"cuisine", "masterDisease"})
public class ChronicDisease extends BaseEntity implements Serializable {

    /**
     * 适宜疾病code
     *编码
     */
    private String code;

    /**
     * 禁忌疾病code
     */
    private String prohibitionCode;

    @Column(columnDefinition = "text")
    private String matterDishesKey;

    @Column(columnDefinition = "text")
    private String prohibitionMatterDishesKey;

    /**
     * 名称
     */
    private String name;

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

    @ManyToOne
    @JoinColumn(name = "master_id")
    private MasterDisease masterDisease;

    /**
     * 对应食谱
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chronic_disease_cuisine", joinColumns = {
            @JoinColumn(name = "chronic_disease_id", referencedColumnName = "id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "cuisine_id", referencedColumnName = "id")
    })
    private Set<SysCuisine> cuisine;
}
