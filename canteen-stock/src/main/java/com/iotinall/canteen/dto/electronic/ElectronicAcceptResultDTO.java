package com.iotinall.canteen.dto.electronic;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 验收返回参数
 *
 * @author loki
 * @date 2021/05/27 19:47
 */
@Data
@Accessors(chain = true)
public class ElectronicAcceptResultDTO implements Serializable {
    private Long billId;
    private Long goodsId;
    private String billNo;
    private String acceptImgUrl;
}
