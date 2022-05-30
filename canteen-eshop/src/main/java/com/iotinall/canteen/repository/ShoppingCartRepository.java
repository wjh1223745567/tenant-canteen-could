package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author WJH
 * @date 2019/11/2311:26
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>, JpaSpecificationExecutor<ShoppingCart> {

    ShoppingCart findByOrgEmployeeId(Long id);

}
