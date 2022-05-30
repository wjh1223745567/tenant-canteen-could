package com.iotinall.canteen.dto.orgemployee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.jsr303.IdCard;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

/**
 * 添加组织员工请求
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Data
@ApiModel(description = "添加组织员工请求")
public class OrgEmployeeAddReq {

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "请填写姓名")
    private String name;// 姓名

    @ApiModelProperty(value = "性别", required = true)
    @NotNull(message = "请填写性别")
    private Integer gender;// 性别

    @ApiModelProperty(value = "身份证号")
    @IdCard
    private String idNo;// 身份证号

    @ApiModelProperty(value = "用户头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String avatar;

    @ApiModelProperty(value = "手机号码")
    private String mobile;// 手机号码

    @ApiModelProperty(value = "人员类型")
    @NotNull(message = "请选择人员类型")
    private Integer personnelType;

    @ApiModelProperty(value = "电话号码")
    private String telephone;// 电话号码

    @ApiModelProperty(value = "角色")
    private String role;// 角色

    @ApiModelProperty(value = "是否启用", required = true)
    @NotNull(message = "请填写是否启用")
    private Boolean enabled;// 是否启用

    @ApiModelProperty(value = "组织id", required = true)
    @NotNull(message = "请选择组织")
    private Long orgId;// 组织id

    @ApiModelProperty(value = "入职日期", required = true)
    private LocalDate entryDate;

    @ApiModelProperty(value = "卡号", required = true)
    @NotBlank(message = "请输入卡号")
    private String cardNo;

    /**
     * 角色ID
     */
    @NotEmpty(message = "请选择用户角色")
    private List<Long> addRoleIds;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "请输入密码")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,10}$", message = "密码不规范")
    private String pwd;
}