package com.iotinall.canteen.dto.orgemployee;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Data
@ApiModel(description = "查询组织员工条件")
public class OrgEmployeeQueryCriteria {
    private Long orgId;
    private Integer type;
    private String keyword;
}