package com.iotinall.canteen.dto.dish;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 主菜系表 存储菜系所属主菜系 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 11:08
 */
@Data
@ApiModel(value = "主菜系表")
public class SysMasterCuisineDTO implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "菜系名称")
    private String name;

    @ApiModelProperty(value = "菜系子类")
    private List<SysCuisineDTO> cuisines;
}
