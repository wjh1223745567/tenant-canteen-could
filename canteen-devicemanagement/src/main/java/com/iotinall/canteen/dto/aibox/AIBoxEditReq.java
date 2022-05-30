package com.iotinall.canteen.dto.aibox;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 编辑摄像头
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AIBoxEditReq extends AIBoxAddReq {
    private Long id;
}