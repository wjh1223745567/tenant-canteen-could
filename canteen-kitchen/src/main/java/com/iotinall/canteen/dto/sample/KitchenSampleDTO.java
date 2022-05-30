package com.iotinall.canteen.dto.sample;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author xinbing
 * @date 2020-07-06 17:09:03
 */
@Data
@ApiModel(description = "留样管理DTO")
public class KitchenSampleDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "图片", required = true)
    //@JsonSerialize(using = ImgPair.ImgSerializer.class)
    private List<String> img;

    @ApiModelProperty(value = "检查时间", required = true)
    private LocalDateTime recordTime; // 检查时间

    @ApiModelProperty(value = "餐次", required = true)
    private Integer mealType; // 餐次

//    @ApiModelProperty(value = "食物", required = true)
//    private List<KitchenSampleAddReq.Foods> foods; // 食物

    @ApiModelProperty(value = "责任人")
    private List<KitchenSampleDutyEmpDTO> kitchenSampleDutyEmpDTOS;

//    @ApiModelProperty(value = "检查人id", required = true)
//    private Long auditorId; // 检查人

//    @ApiModelProperty(value = "检查人名称")
//    private String auditorName; // 检查人名称

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "留样备注")
    private String comments; // 留样备注

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime; // 创建时间
//
//    @ApiModelProperty(value = "更新时间")
//    private LocalDateTime updateTime;//更新时间

    @ApiModelProperty(value = "留样要求")
    private String requirements;

    private String role;
}