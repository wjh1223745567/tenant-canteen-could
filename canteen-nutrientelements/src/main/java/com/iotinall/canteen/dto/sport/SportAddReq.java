package com.iotinall.canteen.dto.sport;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 添加运动记录请求参数
 *
 * @author loki
 * @date 2020/04/14 9:22
 */
@ApiModel(value = "添加运动记录请求参数")
@Data
public class SportAddReq implements Serializable {
    /**
     * 运动名称
     */
    @NotBlank(message = "运动项目名称不能为空")
    private String name;
    /**
     * 运动时间 单位秒
     */
    @NotNull(message = "运动时间不能为空")
    private Integer time;
    /**
     * 燃烧热量
     */
    @NotNull(message = "热量不能为空")
    private BigDecimal burnCalories;

    @NotNull(message = "运动日期不能为空")
    private LocalDate sportDate;
}
