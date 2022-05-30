package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 食堂商品
 *
 * @author xin-bing
 * @date 10/10/2019 20:35
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "mess_takeout_product_info")
@SQLDelete(sql = "update mess_product set deleted = 1 where id = ?")
public class MessTakeoutProductInfo extends BaseEntity implements Serializable {

    /**
     * 商品编号
     */
    @Column(unique = true, nullable = false, length = 50)
    private String serialCode;

    @Column(nullable = false, length = 128)
    private String name; // 商品名称

    @Column(nullable = false)
    private String unit;

    /**
     * 规格型号
     */
    @Column(nullable = false)
    private String specificationModel;

    private String img; // 图片

    private Boolean deleted;

    /**
     * 租户ID，所属哪个食堂
     */
    @Column(nullable = false, updatable = false)
    private Long tenantId;

}
