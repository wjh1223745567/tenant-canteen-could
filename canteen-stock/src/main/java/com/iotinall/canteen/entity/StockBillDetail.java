package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 出入库明细（采购入库、领用出库、领用退库）
 *
 * @author loki
 * @date 2020/04/30 18:17
 */
@Data
@Entity
@Table(name = "stock_bill_detail")
@ToString(exclude = {"bill"})
@JsonIgnoreProperties(value = "bill")
@EqualsAndHashCode(exclude = {"bill"}, callSuper = true)
public class StockBillDetail extends BaseEntity {
    /**
     * 申请单据
     */
    @ManyToOne
    @JoinColumn(name = "bill_id", foreignKey = @ForeignKey(name = "null"))
    @NotFound(action = NotFoundAction.IGNORE)
    private StockBill bill;

    /**
     * 商品
     */
    @OneToOne
    @JoinColumn(name = "goods_id", foreignKey = @ForeignKey(name = "null"))
    @NotFound(action = NotFoundAction.IGNORE)
    private StockGoods goods;

    /**
     * 申请数量
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    /**
     * 实际数量
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal realAmount;

    /**
     * 申请时库存数
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal stockAmount;

    /**
     * 单价
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    /**
     * 检测报告 true - 有 ，false -无
     */
    private Boolean inspectionReport;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 保质日期,整形数字
     */
    private Integer shelfLife;

    /**
     * 保质期单位 0-年 1-月 2-日
     */
    private Integer shelfLifeUnit;

    /**
     * 保质日期，由shelfLife 和 shelfLifeUnit 计算而得的日期
     */
    private LocalDate shelfLifeDate;

    /**
     * 是否验收 true-已经验收 false-未验收
     */
    private Boolean acceptance = false;

    /**
     * 验收图片
     */
    @Column(length = 128)
    private String imgUrl;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 商品一键出库 对应入库单据明细
     * 商品退库，对应的入库单据
     */
    @OneToOne
    @JoinColumn(name = "bill_detail_id", foreignKey = @ForeignKey(name = "null"))
    @NotFound(action = NotFoundAction.IGNORE)
    private StockBillDetail billDetail;

    /******************************************************************************************
     电子秤相关参数
     ****************************************************************************************/

    /**
     * 电子秤拍照原图片路径，防止电子秤缓存丢失，显示验收图片问题
     */
    private String originalImgPath;

    /**
     * 是否已经称重 true-已经称重 false-未称重
     */
    private Boolean weighed = false;
}
