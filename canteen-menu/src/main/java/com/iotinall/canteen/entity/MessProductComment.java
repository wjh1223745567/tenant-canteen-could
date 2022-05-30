package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品描述
 * @author WJH
 * @date 10/10/2019 20:42
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString()
@Entity
@Table(name = "mess_product_comment")
public class MessProductComment extends BaseEntity implements Serializable {

    private String content; // 评价内容

    @Column(nullable = false, columnDefinition = "decimal(2,1)")
    private BigDecimal score; // 评分

    @Column
    private Integer favorCount; // 点赞数

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    private String favorEmpId;

    @Column
    private Integer oppositeCount;// 反对数

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    private String oppositeEmpId;

    @Column(nullable = false)
    private Boolean anonymous; // 是否匿名

    private String tags; // 评论的标签，多个逗号分隔

    @Column(nullable = false)
    private Long productId; // 产品id

    @Column(name = "emp_id")
    private Long employeeId; // 点评人
}
