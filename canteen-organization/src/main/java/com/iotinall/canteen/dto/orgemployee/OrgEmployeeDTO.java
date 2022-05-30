package com.iotinall.canteen.dto.orgemployee;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Data
@ApiModel(description = "返回组织员工结果")
public class OrgEmployeeDTO implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "性别", required = true)
    private Integer gender;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "头像", required = true)
    private String avatarFace;

    @ApiModelProperty(value = "头像", required = true)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String avatar;

    /**
     * 人员类型
     */
    private Integer personnelType;

    private String personnelTypeName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    private LocalDate entryDate;

    @ApiModelProperty(value = "角色")
    private String role;

    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    private LocalDate birthday;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    /**
     * 角色ID
     */
    private List<Long> roleIds;

    @ApiModelProperty(value = "组织id", required = true)
    private Long orgId;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    private String orgFullName;
}