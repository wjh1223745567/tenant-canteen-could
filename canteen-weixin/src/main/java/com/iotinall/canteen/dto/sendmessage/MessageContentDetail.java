package com.iotinall.canteen.dto.sendmessage;

import lombok.Data;

import java.io.Serializable;

/**
 * 详细详情内容
 *
 * @author loki
 * @date 2021/04/17 18:16
 */
@Data
public class MessageContentDetail implements Serializable {
    private String labelName;
    private String labelValue;

    public MessageContentDetail(String labelName, String labelValue) {
        this.labelName = labelName;
        this.labelValue = labelValue;
    }
}
