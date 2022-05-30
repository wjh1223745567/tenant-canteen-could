package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.Nutrition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 货品
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_goods")
@ToString(exclude = {"stock"})
@EqualsAndHashCode(exclude = {"stock"}, callSuper = false)
@JsonIgnoreProperties(value = "stock")
public class StockGoods extends Nutrition {
    /**
     * 货品编号,系统生成,用户不可修改，格式：G+类型编号+四位随机字符串
     */
    @Column(length = 32)
    private String code;

    /**
     * 货品名称
     */
    @Column(length = 64)
    private String name;

    /**
     * 货品别名
     */
    private String nickname;

    /**
     * 拼音
     */
    private String namePinYin;

    /**
     * 名称首字母
     */
    private String nameFirstLetter;

    /**
     * 货品图片
     */
    @Column(length = 128)
    private String imgUrl;

    /**
     * 货品规格
     */
    @Column(length = 32)
    private String specs;

    /**
     * 货品单位
     */
    private String unit;

    /**
     * 当前实际单价，随着入库单价的变化而变化
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    /**
     * 质量
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal quality;

    /**
     * 参考价格
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal referencePrice;

    /**
     * 库存下线
     */
    private BigDecimal lowerLimit;

    /**
     * 货品类别
     */
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private StockGoodsType type;

    /**
     * 仓库
     */
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private StockWarehouse warehouse;

    /**
     * 库存
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Stock stock;

    /**
     * 数据源ID
     */
    @Column(length = 200, unique = true)
    private String dishMaterialId;

    /**
     * 供应商(冗余最新的供应商)
     */
    @OneToOne
    @JoinColumn(name = "supplier_id")
    private StockSupplier supplier;
}
