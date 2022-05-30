package com.iotinall.canteen.dto.mine;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author WJH
 * @date 2019/11/59:51
 */
@Setter
@Getter
@Accessors(chain = true)
@ApiModel(description = "我的个人信息查询")
public class UserInfoDto {

    @ApiModelProperty(value = "id", name = "ID")
    private Long id;

    @ApiModelProperty(value = "头像", name = "imgHead")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgHead;

    private Integer gender;

    @ApiModelProperty(value = "真实姓名", name = "actualName")
    private String actualName;

    @ApiModelProperty(value = "员工编号", name = "employeeCode")
    private String employeeCode;

    @ApiModelProperty(value = "所属组织", name = "orgName")
    private String orgName;

    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    @ApiModelProperty(value = "工作电话", name = "telephone")
    private String telephone;

    @ApiModelProperty(value = "移动电话", name = "phone")
    private String phone;

}