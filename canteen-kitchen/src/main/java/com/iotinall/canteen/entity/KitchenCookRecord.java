package com.iotinall.canteen.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kitchen_cook_record")
public class KitchenCookRecord extends BaseEntity {
    @Column(nullable = false)
    private LocalDateTime recordTime;

    @Column(nullable = false)
    private Integer mealType;

    private Long messProductId;

    private String messProductName;

    private Long dutyEmpId;

    private String dutyEmpName;

    private Long auditorId;

    private String auditorName;

    @Column(nullable = false)
    private Integer state;

    @Column
    private String comments;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String img;
}
