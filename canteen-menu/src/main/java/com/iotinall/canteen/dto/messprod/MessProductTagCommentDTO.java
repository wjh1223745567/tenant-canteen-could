package com.iotinall.canteen.dto.messprod;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xin-bing
 * @date 2019-10-23 11:35:24
 */
@Data
@NoArgsConstructor
@ApiModel(description = "点评统计")
public class MessProductTagCommentDTO implements Serializable {
    @ApiModelProperty(value = "展示值", required = true)
    private String label;


    @ApiModelProperty(value = "点评总数")
    private Integer count;

}