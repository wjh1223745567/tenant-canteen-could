package com.iotinall.canteen.dto.custom;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 自定义食物对象
 *
 * @author loki
 * @date 2020/04/13 14:04
 */
@Accessors(chain = true)
@Data
public class CustomDishDTO implements Serializable {
    private Long id;
    private String name;
    private Long energy;
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;
}
