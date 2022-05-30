package com.iotinall.canteen.dto.passage;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通行设备添加请求参数
 *
 * @author loki
 * @date 2021/7/9 12:03
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class PassageEditReq extends PassageAddReq {
    private Long id;
}
