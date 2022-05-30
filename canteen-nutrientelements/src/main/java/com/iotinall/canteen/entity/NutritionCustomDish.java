package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.common.entity.NutritionNone;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.service.FeignDishService;
import com.iotinall.canteen.service.FeignMessProductService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 自定义食物,自定义食物目前只支持选择dish表中的数据
 *
 * @author loki
 * @date 2020/04/09 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "nutrition_custom_dish")
public class NutritionCustomDish extends BaseEntity {

    private String name;

    private String img;

    @Column(name = "dish_id")
    private String dishId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "employee_id")
    private Long employeeId;

    public NutritionNone getNutrition(){
        if(dishId != null){
            FeignDishService feignDishService = SpringContextUtil.getBean(FeignDishService.class);
            return feignDishService.findById(dishId);
        }
        if(productId != null){
            FeignMessProductService messProductService = SpringContextUtil.getBean(FeignMessProductService.class);
            return messProductService.findById(productId);
        }
        return null;
    }
}
