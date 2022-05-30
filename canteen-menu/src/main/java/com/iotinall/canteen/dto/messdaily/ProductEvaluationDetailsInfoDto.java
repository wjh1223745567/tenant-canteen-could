package com.iotinall.canteen.dto.messdaily;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 菜品内容
 */
@Data
@Accessors(chain = true)
public class ProductEvaluationDetailsInfoDto {

    /**
     * 工艺
     */
    @ApiModelProperty(name = "工艺", value = "craft")
    private String craft;
    /**
     * 口味
     */
    @ApiModelProperty(name = "口味", value = "flavour")
    private String flavour;

    /**
     * 口感
     */
    @ApiModelProperty(name = "口感", value = "taste")
    private String taste;

    /**
     * 类别,多选
     */
    @ApiModelProperty(name = "类别,多选", value = "cuisines")
    private Set<String> cuisines;

}
