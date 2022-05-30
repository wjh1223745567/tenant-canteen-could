package com.iotinall.canteen.dto.sport;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 添加运动记录请求参数
 *
 * @author loki
 * @date 2020/04/14 9:22
 */
@ApiModel(value = "添加运动记录请求参数")
@Data
public class SportEditReq implements Serializable {
    /**
     * 运动记录ID
     */
    @NotNull(message = "主键ID不能为空")
    private Long id;
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
}
