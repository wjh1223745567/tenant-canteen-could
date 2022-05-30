package com.iotinall.canteen.entity;


import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

/**
 * 货品类型
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_goods_type")
@EqualsAndHashCode(callSuper = true, exclude = {"children"})
public class StockGoodsType extends BaseEntity {
    /**
     * 类型编号(T+四位数字 例如T0001)
     */
    private String code;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 父节点
     */
    private Long pid;

    /**
     * 子组织
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pid")
    @OrderBy("id asc ")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<StockGoodsType> children;
}
