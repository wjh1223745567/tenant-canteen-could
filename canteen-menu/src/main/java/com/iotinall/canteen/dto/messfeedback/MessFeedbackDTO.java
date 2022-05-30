package com.iotinall.canteen.dto.messfeedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 *
 * @author xin-bing
 * @date 2019-10-26 17:23:08
 */
@Data
@ApiModel(description = "意见反馈列表")
public class MessFeedbackDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;// id

    @ApiModelProperty(value = "内容")
    private String content;// content

    @ApiModelProperty(value = "是否匿名")
    private Boolean anonymous;// anonymous

    @ApiModelProperty(value = "反馈类型")
    private String feedType;// feed_type

    @ApiModelProperty(value = "状态")
    private Integer status;// status

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;// handle_time

    @ApiModelProperty(value = "处理意见")
    private String handleOpinion;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;// createTime

    @ApiModelProperty(value = "员工id")
    private Long empId;// emp_id

    @ApiModelProperty(value = "员工姓名")
    private String empName;

    @ApiModelProperty(value = "手机号码")
    private String mobile; // 手机号码

    @ApiModelProperty(value = "办公电话")
    private String telephone;

    @ApiModelProperty(value = "职位")
    private String role; // 职位

    @ApiModelProperty(value = "性别，0女，1男")
    private Integer gender;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "组织id")
    private Long orgId; // 组织id

    @ApiModelProperty(value = "组织名称")
    private String orgName; // 组织名称
}