package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "mess_product_cuisine")
public class MessProductCuisine extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private MessProduct product;

    @Column(name = "cuisine_id", nullable = false)
    private String cuisineId;

    @Column(length = 300)
    private String cuisineName;

}
