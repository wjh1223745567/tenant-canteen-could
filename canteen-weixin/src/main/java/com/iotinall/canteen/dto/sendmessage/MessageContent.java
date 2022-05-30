package com.iotinall.canteen.dto.sendmessage;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 详细详情内容
 *
 * @author loki
 * @date 2021/04/17 18:16
 */
@Data
public class MessageContent implements Serializable {
    private String typeName;
    private String createTime;
    private List<MessageContentDetail> content;
    private String redirect;
}
