package com.iotinall.canteen.dto.escp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取人员列表请求参数
 *
 * @author loki
 * @date 2021/6/21 15:36
 **/
@Data
public class OrgListDTO {
    /**
     * 人员信息id
     */
    @JsonProperty(value = "deptid")
    private Long deptId;

    /**
     * 部门名称
     */
    @JsonProperty(value = "deptname")
    private String deptName;

    /**
     * 父级部门id
     */
    @JsonProperty(value = "parentid")
    private Long parentId;

    /**
     * 是否删除
     */
    @JsonProperty(value = "status")
    private Integer status;

    /**
     * 创建日期 yyyy-MM-dd
     */
    @JsonProperty(value = "createdate")
    private String createDate;

    /**
     * 机构编码
     */
    @JsonProperty(value = "organcode")
    private Long organCode;

    /**
     * 版本号
     */
    private Long ver;
}
