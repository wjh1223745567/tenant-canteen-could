package com.iotinall.canteen.dto.employee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "员工列表")
public class EmpListDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "头像")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String avatar;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "工号")
    private String workNo;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "职位")
    private String role;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "生日时间")
    private LocalDate birthday;

    @ApiModelProperty(value = "入职日期")
    private LocalDate entryDate;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
