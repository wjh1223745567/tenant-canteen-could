package com.iotinall.canteen.dto.orgemployee;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.personalrecords.OrgEmployeePersonalRecordsResp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrgEmployeeView {

    private Long id;// id

    private String name;// 姓名

    private Integer gender;// 性别

    private String idNo;// 身份证号

    @ApiModelProperty(value = "头像", required = true)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String avatar;// 验证图片

    private String mobile;// 手机号码

    private String telephone;// 电话号码

    private LocalDate entryDate;

    private String role;// 角色

    private String cardNo; // 卡号

    private String orgFullName;

    /**
     * 人员档案
     */
    List<OrgEmployeePersonalRecordsResp> personalRecordsRespList;
}
