package com.iotinall.canteen.dto.sysdictdetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author xin-bing
 * @date 2019-10-23 11:35:24
 */
@Data
@ApiModel(description = "字典列表")
public class SysDictDetailDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;// id

    @ApiModelProperty(value = "展示值", required = true)
    private String label;// label

    @ApiModelProperty(value = "实际值", required = true)
    private String value;// value

    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;// sort

    @ApiModelProperty(value = "类型，1为系统默认的，不允许删除", required = true)
    private Integer typ;// typ

    private String remark;

    @ApiModelProperty(value = "字典组", required = true)
    private String groupCode;// group_id

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}