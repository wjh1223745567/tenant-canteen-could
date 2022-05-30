package com.iotinall.canteen.dto.kitchen;

import lombok.Data;

import java.io.Serializable;


/**
 * 留样记录图片
 *
 * @author loki
 * @date 2021/7/14 11:45
 **/
@Data
public class FeignKitchenSampleDTO implements Serializable {
    private String imgUrl;
    private String mealTypeName;
}