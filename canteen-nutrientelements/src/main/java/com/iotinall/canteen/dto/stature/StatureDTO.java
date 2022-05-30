package com.iotinall.canteen.dto.stature;

import com.iotinall.canteen.dto.sharingscale.StandardSubRemarkDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 身材记录返回对象
 *
 * @author loki
 * @date 2020/04/10 17:14
 */
@Data
@Accessors(chain = true)
public class StatureDTO {
    private String code;
    private String name;
    private String standard;
    private BigDecimal value;
    private String unit;

    private List<StandardSubRemarkDto> remarkDtoList;

    private String remark;

    private List<Double> scaleValue;

    private LocalDateTime createTime;
}
