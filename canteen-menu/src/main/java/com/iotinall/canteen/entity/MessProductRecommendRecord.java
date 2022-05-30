package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 推荐记录表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "mess_product_recommendRecord", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_date", columnList = "date")
})
public class MessProductRecommendRecord extends BaseEntity {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "date")
    private String date; // 日期，一天一条

    @Column(name = "recommend_count")
    private Integer recommendCount; // 推荐次数

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer praiseCount; // 好评数

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer negativeCount; // 差评数

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "emp_list", columnDefinition = "text")
    private String empList;
}
