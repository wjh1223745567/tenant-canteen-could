package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constants.TakeoutStatus;
import com.iotinall.canteen.dto.shopping.ShoppingCartDto;
import com.iotinall.canteen.dto.shopping.ShoppingCartProductDto;
import com.iotinall.canteen.dto.shopping.ShoppingCartReq;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.repository.MessTakeoutOrderRepository;
import com.iotinall.canteen.repository.MessTakeoutProductStockRepository;
import com.iotinall.canteen.repository.ShoppingCartProductRepository;
import com.iotinall.canteen.repository.ShoppingCartRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author WJH
 * @date 2019/11/2311:29
 */
@Service
public class ShoppingCartService {

    @Resource
    private ShoppingCartRepository shoppingCartRepository;

    @Resource
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Resource
    private MessTakeoutOrderRepository messTakeoutOrderRepository;

    @Resource
    private MessTakeoutProductStockRepository messTakeoutProductStockRepository;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    @Resource
    private MessTakeoutProductStockRepository productStockRepository;

    @Transactional(rollbackFor = Exception.class)
    public ShoppingCartDto findShoppingCart(Long empId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByOrgEmployeeId(empId);
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        List<ShoppingCartProductDto> products = new ArrayList<>();
        if (shoppingCart != null && shoppingCart.getCartProducts() != null) {
            for (ShoppingCartProduct item : shoppingCart.getCartProducts()) {
                if (item.getMessProduct() != null) {
                    MessTakeoutProductInfo messProduct = item.getMessProduct().getMessProduct();
                    ShoppingCartProductDto product = new ShoppingCartProductDto()
                            .setId(item.getMessProduct().getId())
                            .setCount(item.getCount())
                            .setImage(messProduct.getImg())
                            .setName(messProduct.getName())
                            .setPrice(item.getMessProduct().getPrice());
                    products.add(product);
                }
            }
        }
        shoppingCartDto.setProducts(products);
        return shoppingCartDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.appShoppingCart, RedisCacheUtil.MESS_TAKEOUT_PRODUCT}, allEntries = true)
    public void updateShoppingCart(ShoppingCartReq shoppingCartReqs) {
        Long empId = SecurityUtils.getUserId();

        ShoppingCart shoppingCart = this.shoppingCartRepository.findByOrgEmployeeId(empId);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setOrgEmployeeId(empId);
        }
        ShoppingCart oldCart = this.shoppingCartRepository.save(shoppingCart);

        if (oldCart.getCartProducts() == null) {
            oldCart.setCartProducts(new ArrayList<>());
        }
        ShoppingCartProduct cartProduct = this.shoppingCartProductRepository.findFirstByShoppingCartAndMessProduct_Id(oldCart, shoppingCartReqs.getProductId());

        MessTakeoutProductStock messProduct = this.productStockRepository.findById(shoppingCartReqs.getProductId()).orElse(null);
        if (messProduct == null && cartProduct != null) {
            this.shoppingCartProductRepository.deleteById(cartProduct.getId());
        } else if (messProduct != null) {
            if (shoppingCartReqs.getCount() > (messProduct.getCount() - (messProduct.getSalesVolume() != null ? messProduct.getSalesVolume() : 0))) {
                throw new BizException("", "当前商品库存不足！");
            }
            this.limitCount(empId, messProduct, shoppingCartReqs.getCount());
            if (cartProduct != null && shoppingCartReqs.getCount() > 0) {
                cartProduct.setCount(shoppingCartReqs.getCount());
                this.shoppingCartProductRepository.save(cartProduct);
            } else if (cartProduct != null && shoppingCartReqs.getCount() <= 0) {
                this.shoppingCartProductRepository.deleteById(cartProduct.getId());
            } else if (cartProduct == null && shoppingCartReqs.getCount() > 0) {
                cartProduct = new ShoppingCartProduct()
                        .setCount(shoppingCartReqs.getCount())
                        .setMessProduct(messProduct)
                        .setShoppingCart(shoppingCart);
                this.shoppingCartProductRepository.save(cartProduct);
            }
        }
    }

    /**
     * 限购
     *
     * @author hjj
     * @date 2021/04/29 15:00
     */
    public void limitCount(Long employeeId, MessTakeoutProductStock messTakeoutProductStock, Integer reqCount) {
        //找出用户已支付和待支付的所有订单
        List<MessTakeoutOrder> orderList = this.messTakeoutOrderRepository.findByEmployeeIdAndSourcingStatusIn(employeeId, Arrays.asList(TakeoutStatus.PAID, TakeoutStatus.UNPAID));

        //查找未完成订单
        Integer todayCount = 0;
        for (MessTakeoutOrder messTakeoutOrder : orderList) {
            Set<MessTakeoutOrderDetail> messTakeoutOrderDetails = messTakeoutOrder.getMessTakeoutOrderDetails();
            if (!CollectionUtils.isEmpty(messTakeoutOrderDetails)) {
                //比较菜品和未完成订单菜品数量是否超额
                for (MessTakeoutOrderDetail detail : messTakeoutOrderDetails) {
                    if (detail.getMessProduct().getId().equals(messTakeoutProductStock.getId())) {
                        todayCount = todayCount + detail.getCount();
                    }
                }
            }
        }

        /**
         * limitcount = 0 时代表不限购
         */
        if (messTakeoutProductStock.getLimitCount() != 0
                && todayCount + reqCount > messTakeoutProductStock.getLimitCount()) {
            throw new BizException("", "商品" + messTakeoutProductStock.getMessProduct().getName() + "超过限购数量");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.appShoppingCart, RedisCacheUtil.MESS_TAKEOUT_PRODUCT}, allEntries = true)
    public void removeShoppingCart() {
        Long empid = SecurityUtils.getUserId();
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByOrgEmployeeId(empid);
        if (shoppingCart != null) {
            if (!CollectionUtils.isEmpty(shoppingCart.getCartProducts())) {
                this.shoppingCartProductRepository.deleteInBatch(shoppingCart.getCartProducts());
            }
            this.shoppingCartRepository.delete(shoppingCart);
        }
    }
}
