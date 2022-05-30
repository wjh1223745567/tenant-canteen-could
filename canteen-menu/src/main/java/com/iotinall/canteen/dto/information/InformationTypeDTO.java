package com.iotinall.canteen.dto.information;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author WJH
 * @date 2019/11/19:55
 */
@Setter
@Getter
@Accessors(chain = true)
public class InformationTypeDTO {

    private Long id;

    private String name;

    private Integer infoCount;

    private String remark;

    private Boolean status;

    private LocalDateTime createTime;

    private String bindOrgList;
}
