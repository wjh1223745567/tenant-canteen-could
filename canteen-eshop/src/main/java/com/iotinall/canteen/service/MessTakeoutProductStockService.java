package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.tackout.MessProductDto;
import com.iotinall.canteen.dto.tackout.MessProductImagesResp;
import com.iotinall.canteen.dto.tackout.MessProductTypeDto;
import com.iotinall.canteen.entity.MessTakeoutProductInfo;
import com.iotinall.canteen.entity.MessTakeoutProductStock;
import com.iotinall.canteen.entity.ShoppingCart;
import com.iotinall.canteen.entity.ShoppingCartProduct;
import com.iotinall.canteen.repository.MessTakeoutProductStockRepository;
import com.iotinall.canteen.repository.MessTakeoutProductTypeRepository;
import com.iotinall.canteen.repository.ShoppingCartProductRepository;
import com.iotinall.canteen.repository.ShoppingCartRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/2315:01
 */
@Service
public class MessTakeoutProductStockService {
    @Resource
    private MessTakeoutProductStockRepository productStockRepository;

    @Resource
    private ShoppingCartRepository shoppingCartRepository;

    @Resource
    private MessTakeoutProductTypeRepository messTakeoutProductTypeRepository;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    public List<MessProductDto> findProduct(Long empid, String name, Long productTypeId) {
        Specification<MessTakeoutProductStock> spec = SpecificationBuilder.builder()
                .where(Criterion.like("messProduct.name", name),
                        Criterion.eq("productType.id", productTypeId),
                        Criterion.eq("deleted", Boolean.FALSE),
                        Criterion.eq("state", Boolean.TRUE),
                        Criterion.eq("tenantId", feignTenantOrganizationService.findMenuId())
                )
                .build();
        List<MessTakeoutProductStock> outProduct = this.productStockRepository.findAll(spec, Sort.by(Sort.Order.desc("top")));
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByOrgEmployeeId(empid);
        List<MessProductDto> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(outProduct)) {
            for (MessTakeoutProductStock item : outProduct) {
                MessTakeoutProductInfo product = item.getMessProduct();
                Integer count = 0;
                if (shoppingCart != null && !CollectionUtils.isEmpty(shoppingCart.getCartProducts())) {
                    count = shoppingCart.getCartProducts().stream().filter(cartProduct -> cartProduct.getMessProduct().equals(item)).findFirst().orElse(new ShoppingCartProduct().setCount(0)).getCount();
                }
                if (item.getCount() - item.getSalesVolume() < 0) {
                    continue;
                }
                MessProductDto messProductDto = new MessProductDto()
                        .setId(item.getId())
                        .setTackOutId(item.getId())
                        .setName(product.getName())
                        .setImage(product.getImg())
                        .setTackOutTypeName(item.getProductType() != null ? item.getProductType().getName() : null)
                        .setPrice(item.getPrice())
                        .setStock(item.getCount() - (item.getSalesVolume() != null ? item.getSalesVolume() : 0))
                        .setLimitCount(item.getLimitCount())
                        .setNumberOfShoppingCarts(count);
                result.add(messProductDto);
            }
        }
        return result;
    }

    /**
     * 按名称检索
     *
     * @param name
     * @return
     */
    public List<String> findTakeoutProductName(String name) {
        Specification<MessTakeoutProductStock> stockSpecification = SpecificationBuilder.builder()
                .where(Criterion.like("messProduct.name", name),
                        Criterion.eq("deleted", Boolean.FALSE),
                        Criterion.eq("state", Boolean.TRUE),
                        Criterion.eq("tenantId", feignTenantOrganizationService.findMenuId()))
                .build();
        return this.productStockRepository.findAll(stockSpecification, Sort.by(Sort.Order.desc("top"))).stream().map(item -> item.getMessProduct().getName()).collect(Collectors.toList());
    }


    /**
     * 搜索照片
     *
     * @return
     */
    public List<MessProductImagesResp> findProductImages() {
        List<MessTakeoutProductStock> outProduct = this.productStockRepository.findAll(SpecificationBuilder.builder()
                .where(
                        Criterion.eq("deleted", Boolean.FALSE),
                        Criterion.eq("state", Boolean.TRUE),
                        Criterion.eq("tenantId", feignTenantOrganizationService.findMenuId())
                )
                .build(), Sort.by(Sort.Order.desc("top")));
        List<MessProductImagesResp> imagesResps = new ArrayList<>(5);
        if (outProduct.size() == 0) {
            return Collections.emptyList();
        }
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            MessTakeoutProductStock stock = outProduct.get(random.nextInt(outProduct.size()));
            imagesResps.add(
                    new MessProductImagesResp()
                            .setId(stock.getId())
                            .setName(stock.getMessProduct() != null ? stock.getMessProduct().getName() : null)
                            .setUrl(stock.getMessProduct() != null ? stock.getMessProduct().getImg() : null)
            );
        }
        return imagesResps;
    }


    public MessProductDto findById(Long id) {
        MessTakeoutProductStock outProduct = this.productStockRepository.findById(id).orElse(null);
        if (outProduct == null) {
            throw new BizException("", "未找到当前外购商品");
        }
        Long empid = SecurityUtils.getUserId();
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByOrgEmployeeId(empid);

        MessTakeoutProductInfo messProduct = outProduct.getMessProduct();
        Integer count = 0;
        if (shoppingCart != null && !CollectionUtils.isEmpty(shoppingCart.getCartProducts())) {
            count = shoppingCart.getCartProducts().stream().filter(cartProduct -> Objects.equals(cartProduct.getMessProduct().getMessProduct().getId(), (messProduct.getId()))).findFirst().orElse(new ShoppingCartProduct().setCount(0)).getCount();
        }

        return new MessProductDto()
                .setId(outProduct.getId())
                .setImage(messProduct.getImg())
                .setUnit(messProduct.getUnit())
                .setSpecificationModel(messProduct.getSpecificationModel())
                .setRemark(outProduct.getRemark())
                .setStock(outProduct.getCount() - outProduct.getSalesVolume())
                .setLimitCount(outProduct.getLimitCount())
                .setName(messProduct.getName())
                .setNumberOfShoppingCarts(count)
                .setPrice(outProduct.getPrice());
    }

    /**
     * 按分类查询
     *
     * @return
     */
    public List<MessProductTypeDto> findAllType() {
        return this.messTakeoutProductTypeRepository.findAllByParentIsNullAndTenantId(feignTenantOrganizationService.findMenuId()).stream().map(item ->
                new MessProductTypeDto()
                        .setId(item.getId())
                        .setName(item.getName())
        ).collect(Collectors.toList());
    }

}
