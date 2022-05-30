package com.iotinall.canteen.dto.emptyplate;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 光盘行动消息推送配置
 *
 * @author loki
 * @date 2021/7/7 11:16
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class EmptyPlateMsgConfigEditReq extends EmptyPlateMsgConfigAddReq {
    private Long id;
}
