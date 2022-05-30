package com.iotinall.canteen.dto.disease;

import lombok.Data;

import java.util.List;

/**
 * 菜品类型 ,作为字典使用,不允许增删改
 *
 * @author loki
 * @date 2020/03/25 9:35
 */
@Data
public class CuisineDTO {
    private String id;
    private String name;
    private String code;

    private List<CuisineDTO> cuisine;
}
