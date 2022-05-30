package com.iotinall.canteen.dto.foodnotetype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 美食笔记类型DTO
 * @author: JoeLau
 * @time: 2021年07月06日 13:55:01
 */
@Data
@ApiModel(description = "返回美食笔记类型DTO")
public class FoodNoteTypeDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    /**
     * 类型名
     */
    @ApiModelProperty(value = "类型名称")
    private String name;

    /**
     * 笔记数
     */
    @ApiModelProperty(value = "笔记数")
    private Integer noteNumber;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 状态 true-启用 false-禁用
     */
    @ApiModelProperty(value = "类型状态", notes = "true-启用 false-禁用")
    private Boolean status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
