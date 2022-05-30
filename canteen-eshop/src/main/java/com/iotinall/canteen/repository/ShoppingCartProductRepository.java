package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.MessTakeoutProductStock;
import com.iotinall.canteen.entity.ShoppingCart;
import com.iotinall.canteen.entity.ShoppingCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author WJH
 * @date 2019/11/2311:26
 */
public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Long>, JpaSpecificationExecutor<ShoppingCartProduct> {

    ShoppingCartProduct findFirstByShoppingCartAndMessProduct_Id(ShoppingCart cart, Long messProductId);

    void deleteAllByMessProduct(MessTakeoutProductStock messProduct);

}
