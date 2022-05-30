package com.iotinall.canteen.dto.sendmessage;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMessageTypeDto {

    private Long id;

    private String typeName;

}
