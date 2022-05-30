package com.iotinall.canteen.dto.diet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 添加饮食记录请求参数
 *
 * @author loki
 * @date 2020/04/10 19:09
 */
@Data
@ApiModel(value = "添加饮食记录请求参数")
public class DietAddReq implements Serializable {
    /**
     * 饮食记录
     */
    @ApiModelProperty(value = "饮食记录")
    private List<DietRecordDTO> diets;

    /**
     * 日期
     */
    @ApiModelProperty(value = "饮食日期")
    private LocalDate date;
}
