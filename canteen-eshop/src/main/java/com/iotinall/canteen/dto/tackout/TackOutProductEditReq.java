package com.iotinall.canteen.dto.tackout;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author WJH
 * @date 2019/11/2615:55
 */
@Data
public class TackOutProductEditReq {

    @NotNull
    private Long id;

    @NotBlank
    @Length(max = 128)
    private String name; // 商品名称

    /**
     * 单位
     */
    @NotBlank
    @Length(max = 255)
    private String unit;

    @NotBlank
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 图片

    @NotNull
    private Long typeId;

    @NotBlank
    @Length(max = 255)
    private String specificationModel;

    @NotNull
    @Min(0)
    @Max(50000)
    private Integer limitCount;

    /**
     * 库存数量
     */
    @NotNull
    @Min(0)
    @Max(50000)
    private Integer count;

    /**
     * 价格
     */
    @NotNull
    private BigDecimal price;

    /**
     * 状态
     */
    @NotNull
    private Boolean state;

    // 备注
    @NotBlank
    private String remark;

    @NotNull
    private Boolean top;

}
