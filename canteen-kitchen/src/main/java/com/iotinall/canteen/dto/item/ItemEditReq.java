package com.iotinall.canteen.dto.item;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "项目修改")
public class ItemEditReq {
    @ApiModelProperty(value = "主键")
    @NotNull(message = "项目id不能为空")
    private Long id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

//    @ApiModelProperty(value = "描述")
//    @NotBlank(message = "描述不能为空")
//    private String description;

    @ApiModelProperty(value = "责任人ID")
    //@NotNull(message = "责任人ID不能为空（可有多个责任人）")
    private List<Long> empIds;

    @ApiModelProperty(value = "排序")
    private int seq;

    @ApiModelProperty(value = "摄像头ID")
    private List<Long> deviceId;

    @ApiModelProperty(value = "要求")
    private String requirements;

    @ApiModelProperty(value = "备注")
    private String comment;

    /**
     * 检查时间，LocalDateTime格式用逗号分隔
     */
    @ApiModelProperty(value = "检查时间")
    @Length(max = 255,message = "检查时间过多")
    private String checkTime;//检查时间
}
