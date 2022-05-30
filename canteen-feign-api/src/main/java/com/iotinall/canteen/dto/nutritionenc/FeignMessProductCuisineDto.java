package com.iotinall.canteen.dto.nutritionenc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜品类别
 *
 * @author loki
 * @date 2020/04/21 18:41
 */
@Data
public class FeignMessProductCuisineDto implements Serializable {
    private String id;
    private String name;
    private Integer num;
    private List<FeignMessProductCuisineDto> children;
}
