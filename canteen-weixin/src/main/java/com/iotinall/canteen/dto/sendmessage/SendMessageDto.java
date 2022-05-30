package com.iotinall.canteen.dto.sendmessage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendMessageDto {

    private Long id;

    private String content;

    private Type typeName;

    private Boolean open;

    private String remark;

    @Setter
    @Getter
    @Accessors(chain = true)
    public class Type {

        private int id;

        private String name;

    }

}
