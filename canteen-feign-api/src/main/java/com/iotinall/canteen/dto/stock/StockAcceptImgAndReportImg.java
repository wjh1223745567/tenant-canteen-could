package com.iotinall.canteen.dto.stock;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 采购验收图片
 *
 * @author loki
 * @date 2021/02/23 20:07
 */
@Data
public class StockAcceptImgAndReportImg implements Serializable {
    private String name;
    private String imgUrl;
    private LocalDateTime updateTime;
}
