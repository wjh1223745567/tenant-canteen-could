package com.iotinall.canteen.dto.passage;

import lombok.Data;

import java.io.Serializable;

/**
 * 通行设备添加请求参数
 *
 * @author loki
 * @date 2021/7/9 12:03
 **/
@Data
public class PassageDTO implements Serializable {
    private Long id;
    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * 详细位置
     */
    private String areaName;

    /**
     * 租户组织ID
     */
    private Long tenantOrgId;

    /**
     * 备注
     */
    private String remark;
}
