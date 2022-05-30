package com.iotinall.canteen.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * feign 请求返回员工对象
 *
 * @author loki
 * @date 2021/06/03 15:32
 */
@Data
@Accessors(chain = true)
public class FeignEmployeeDTO implements Serializable {
    private Long id;
    private String name;
    private String avatar;
    private Long energy;
    private Long orgId;
    private String orgName;
    private String mobile;
    /**
     * 角色ID
     */
    private List<Long> roleIds = new ArrayList<>(0);

    /**
     * 职位
     */
    private String position;

    private String openid;
}
