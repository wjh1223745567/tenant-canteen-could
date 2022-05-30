package com.iotinall.canteen.dto.stature;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 添加身材记录请求参数
 *
 * @author loki
 * @date 2020/04/10 16:21
 */
@Data
@Accessors(chain = true)
public class StatureAddReq implements Serializable {
    /**
     * 登记日期
     */
    private LocalDate date;

    /**
     * 身材数据
     */
    private List<StatureDTO> statures;
}
