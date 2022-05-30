package com.iotinall.canteen.dto.item;

import com.iotinall.canteen.common.jsr303.StrEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(value = "项目添加")
public class ItemAddReq {

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "组编码", allowableValues = "assess_level,rule_type,morning_inspect,disinfect_item,sample_item,wash_type,chop_type,fire_protect" +
            ",facility_item,kitchen_garbage,env_item,food_additives")
    @NotBlank(message = "组编码不能为空")
    @StrEnum(message = "非法的组编码", values = {"assess_level","rule_type","morning_inspect","disinfect_item","sample_item","wash_type","chop_type",
            "fire_protect", "facility_item", "kitchen_garbage", "env_item", "food_additives"})
    private String groupCode;

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
    @NotBlank(message = "要求不能为空")
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
