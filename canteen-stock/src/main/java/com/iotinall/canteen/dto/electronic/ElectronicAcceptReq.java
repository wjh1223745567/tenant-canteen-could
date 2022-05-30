package com.iotinall.canteen.dto.electronic;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 电子秤验收请求参数
 *
 * @author loki
 * @date 2021/05/27 16:59
 */
@Data
public class ElectronicAcceptReq implements Serializable {
    /**
     * 单据ID
     */
    @NotBlank(message = "单据号不能为空")
    private String billNo;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空")
    private Long version;

    /**
     * 电子秤拍照时存在内存中图片路径
     */
    private String originalImgPath;

    /**
     * 是否已经验收
     */
    private Boolean accepted;

    /**
     * 是否已经称重
     */
    private Boolean weighed;

    /**
     * 电子秤称重数量
     */
    private BigDecimal realAmount;

    /**
     * 是否有检测报告
     */
    private Boolean inspectionReport;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 保质期
     */
    private Integer shelfLife;

    /**
     * 保质期单位
     */
    private Integer shelfLifeUnit;
}
