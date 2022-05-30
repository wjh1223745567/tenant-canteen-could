package com.iotinall.canteen.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @description:添加理发师
 * @author: JoeLau
 * @time: 2021年06月24日 13:35:20
 */

@Data
@ApiModel(description = "添加理发师请求参数")
public class HaircutMasterAddReq {

    /**
     * 人员ID
     */
    @ApiModelProperty(value = "理发师人员ID")
    @NotNull(message = "人员ID不能为空")
    private Long empId;

    /**
     * 理发师姓名
     */
    @ApiModelProperty(value = "理发师姓名")
    private String name;

    /**
     * 理发师联系方式
     */
    @ApiModelProperty(value = "理发师手机号码")
    private String phoneNumber;

    /**
     * 所属理发室
     */
    @ApiModelProperty(value = "理发室ID")
    @NotNull(message = "理发室不能为空")
    private Long haircutRoomId;

    /**
     * 参加工作日期
     */
    @ApiModelProperty(value = "参加工作日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workingStart;

    /**
     * 理发师介绍
     */
    @ApiModelProperty(value = "理发师介绍")
    private String presentation;

    /**
     * 头像
     */
    @ApiModelProperty(value = "理发师头像")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String avatar;

}
