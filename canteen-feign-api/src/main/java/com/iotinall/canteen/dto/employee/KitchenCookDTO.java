package com.iotinall.canteen.dto.employee;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author loki
 * @date 2021/01/27 17:54
 */
@Data
@ApiModel(value = "看板厨师信息")
public class KitchenCookDTO {
    @ApiModelProperty(value = "头像")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String avatar;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "体温")
    private Float temperature;

    @ApiModelProperty(value = "体温状态 0-未测量 1-正常 2-不正常")
    private Integer temperatureStatus;

    @ApiModelProperty(value = "职位")
    private String role;

    @ApiModelProperty(value = "证书")
    private List<String> certList;
}
