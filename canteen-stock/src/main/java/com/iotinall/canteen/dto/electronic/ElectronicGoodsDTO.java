package com.iotinall.canteen.dto.electronic;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 电子秤单据对象
 *
 * @author loki
 * @date 2021/05/27 17:15
 */
@Data
public class ElectronicGoodsDTO implements Serializable {
    private Long goodsId;
    private String goodsName;
    private String goodsNamePinYin;
    private BigDecimal amount;
    private BigDecimal realAmount;
    private BigDecimal price;
    private String unit;
    private Boolean inspectionReport;
    private LocalDate productionDate;
    private Integer shelfLife;
    private Integer shelfLifeUnit;
    private String originalImgPath;
    private String acceptImgUrl;
    private String goodsImgUrl;
    private Long detailId;
    private Boolean accepted = false;
    private Boolean uploaded = false;
    private Boolean weighed = false;
    private Long version;
}
