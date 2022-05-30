package com.iotinall.canteen.dto.sysdictdetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加sys_dict_detail请求
 * @author xin-bing
 * @date 2019-10-23 11:35:24
 */
@Data
@ApiModel(description = "添加sys_dict_detail请求")
public class SysDictDetailAddReq {

    @ApiModelProperty(value = "label", required = true)
    @NotBlank(message = "请填写label")
    private String label;// label

    @ApiModelProperty(value = "value", required = true)
    @NotBlank(message = "请填写value")
    private String value;// value

    @ApiModelProperty(value = "sort", required = true)
    @NotNull(message = "请输入排序")
    private Integer sort;// sort

    @ApiModelProperty(value = "字典组", required = true)
    private String groupCode;// groupCode

    @ApiModelProperty(value = "备注")
    private String remark;
}