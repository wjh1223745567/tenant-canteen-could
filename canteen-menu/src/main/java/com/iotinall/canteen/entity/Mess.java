package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 餐厅
 *
 * @author xin-bing
 * @date 10/11/2019 16:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "mess")
public class Mess extends BaseEntity implements Serializable {
    /**
     * 食堂名称
     */
    private String name;

    /**
     * 就餐容量
     */
    private Integer capacity;

    /**
     * 就餐人数
     */
    private Integer diners;
}
