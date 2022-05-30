package com.iotinall.canteen.dto.garbage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author xinbing
 * @date 2020-07-10 11:46:09
 */
@Data
@ApiModel(description = "餐厨垃圾DTO")
public class KitchenGarbageRecordDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "检查区域")
    private String region;

    @ApiModelProperty(value = "图片", required = true)
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img; // 图片

    @ApiModelProperty(value = "检查时间", required = true)
    private LocalDateTime recordTime; // 检查时间

    @ApiModelProperty(value = "餐次", required = true)
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "检查状态", required = true)
    private Integer state; // 检查状态

    @ApiModelProperty(value = "检查备注")
    private String comments; // 检查备注

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间

    @ApiModelProperty(value = "责任人", required = true)
    private List<GarbageDutyEmpDTO> garbageDutyEmpDTOList;

    @ApiModelProperty(value = "检查人id", required = true)
    private Long auditorId; // 检查人id

    @ApiModelProperty(value = "检查人名称")
    private String auditorName;

    //    @ApiModelProperty(value = "分类标准")
//    private List<KitchenItem> itemList;
    @ApiModelProperty(value = "检查项id", required = true)
    private Long itemId; // 检查项id

    @ApiModelProperty(value = "检查项名称")
    private String itemName; // 检查项名称

    @ApiModelProperty(value = "检查项描述")
    private String itemDesc;
}