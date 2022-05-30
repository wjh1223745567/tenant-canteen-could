package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constants.TakeoutStatus;
import com.iotinall.canteen.dto.tackout.TackOutProductAddReq;
import com.iotinall.canteen.dto.tackout.TackOutProductCondition;
import com.iotinall.canteen.dto.tackout.TackOutProductEditReq;
import com.iotinall.canteen.dto.tackout.TakeoutProductDTO;
import com.iotinall.canteen.entity.MessTakeoutProductInfo;
import com.iotinall.canteen.entity.MessTakeoutProductStock;
import com.iotinall.canteen.entity.MessTakeoutProductType;
import com.iotinall.canteen.repository.MessTakeoutOrderDetailRepository;
import com.iotinall.canteen.repository.MessTakeoutProductInfoRepository;
import com.iotinall.canteen.repository.MessTakeoutProductStockRepository;
import com.iotinall.canteen.repository.ShoppingCartProductRepository;
import com.iotinall.canteen.utils.TenantOrgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/2617:15
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SysTackOutProductService {

    @Resource
    private MessTakeoutProductInfoRepository messTakeoutProductInfoRepository;

    @Resource
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Resource
    private MessTakeoutProductStockRepository messTakeoutProductStockRepository;

    @Resource
    private MessTakeoutOrderDetailRepository messTakeoutOrderDetailRepository;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    public PageDTO<TakeoutProductDTO> findByPage(TackOutProductCondition condition, Pageable pageable) {

        SpecificationBuilder spec = SpecificationBuilder.builder()
                .where(Criterion.eq("state", condition.getState()))
                .where(Criterion.like("messProduct.name", condition.getName()))
                .where(Criterion.eq("productType.id", condition.getTypeId()))
                .whereByOr(
                        Criterion.like("messProduct.serialCode",condition.getKeyWords()),
                        Criterion.like("messProduct.name",condition.getKeyWords())
                );

        TenantOrgUtil.getTenantSpec(spec);

        Page<MessTakeoutProductStock> page = this.messTakeoutProductStockRepository.findAll(spec.build(), pageable);
        List<TakeoutProductDTO> productDTOS = page.get().map(item -> new TakeoutProductDTO()
                .setId(item.getId())
                .setName(item.getMessProduct().getName())
                .setSerialCode(item.getMessProduct().getSerialCode())
                .setTypeId(item.getProductType() != null ? item.getProductType().getId() : null)
                .setImg(item.getMessProduct().getImg())
                .setUnit(item.getMessProduct().getUnit())
                .setSpecificationModel(item.getMessProduct().getSpecificationModel())
                .setCount(item.getCount())
                .setSalesVolume(item.getSalesVolume())
                .setState(item.getState())
                .setTop(item.getTop())
                .setPrice(item.getPrice())
                .setLimitCount(item.getLimitCount())
                .setRemark(item.getRemark())
                .setTypeName(item.getProductType() != null ? item.getProductType().getName() : null)).collect(Collectors.toList());
        return PageUtil.toPageDTO(productDTOS, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void toggleProduct(Long id, Boolean state) {
        MessTakeoutProductStock stock = this.messTakeoutProductStockRepository.findById(id).orElseThrow(() -> new BizException("未找到该商品"));
        messTakeoutProductStockRepository.toggleProduct(id, state);
        if(!state){
            log.info("商品" + stock.getMessProduct().getName() + "已下架，开始清空购物车");
            this.shoppingCartProductRepository.deleteAllByMessProduct(stock);
        }
    }

    /**
     * 发布菜品
     */
    public void addProducts(TackOutProductAddReq addReq) {
        Long menuId = TenantOrgUtil.findMenuId();

        MessTakeoutProductInfo messProduct = new MessTakeoutProductInfo()
                .setImg(addReq.getImg())
                .setSerialCode(addReq.getSerialCode())
                .setName(addReq.getName())
                .setDeleted(Boolean.FALSE)
                .setSpecificationModel(addReq.getSpecificationModel())
                .setTenantId(menuId)
                .setUnit(addReq.getUnit());
        this.messTakeoutProductInfoRepository.save(messProduct);

        MessTakeoutProductType productType = new MessTakeoutProductType();
        productType.setId(addReq.getTypeId());
        MessTakeoutProductStock product = new MessTakeoutProductStock()
                .setMessProduct(messProduct)
                .setCount(addReq.getCount())
                .setSalesVolume(0)
                .setDeleted(Boolean.FALSE)
                .setProductType(productType)
                .setPrice(addReq.getPrice())
                .setState(addReq.getState())
                .setTop(addReq.getTop())
                .setTenantId(menuId)
                .setLimitCount(addReq.getLimitCount());
        product.setRemark(addReq.getRemark());
        this.messTakeoutProductStockRepository.save(product);
    }

    public void editProducts(TackOutProductEditReq editReq) {
        Optional<MessTakeoutProductStock> stockOptional = this.messTakeoutProductStockRepository.findById(editReq.getId());
        if (!stockOptional.isPresent()) {
            throw new BizException("", "未找到要编辑的外带商品信息");
        }
        MessTakeoutProductStock stock = stockOptional.get();
        MessTakeoutProductInfo info = stock.getMessProduct()
                .setImg(editReq.getImg())
                .setName(editReq.getName())
                .setSpecificationModel(editReq.getSpecificationModel())
                .setUnit(editReq.getUnit());

        MessTakeoutProductType productType = new MessTakeoutProductType();
        productType.setId(editReq.getTypeId());

        stock
                .setMessProduct(info)
                .setCount(editReq.getCount())
                .setTop(editReq.getTop())
                .setProductType(productType)
                .setPrice(editReq.getPrice())
                .setState(editReq.getState())
                .setLimitCount(editReq.getLimitCount())
                .setRemark(editReq.getRemark());
        this.messTakeoutProductStockRepository.save(stock);
    }

    /**
     * 清空所有已售出的数量
     */
    public void clearAllSell(){
        SpecificationBuilder builder = SpecificationBuilder.builder();
        TenantOrgUtil.getTenantSpec(builder);

        List<MessTakeoutProductStock> productStocks = messTakeoutProductStockRepository.findAll(builder.build());
        for (MessTakeoutProductStock productStock : productStocks) {
            productStock.setSalesVolume(0);
        }
        this.messTakeoutProductStockRepository.saveAll(productStocks);
    }

    public void remove(List<Long> ids){
        if(CollectionUtils.isEmpty(ids)){
            throw new BizException("", "请选择要删除的商品");
        }
        List<MessTakeoutProductStock> stocks = this.messTakeoutProductStockRepository.findAllById(ids);
        for (MessTakeoutProductStock stock : stocks) {
            Long count = this.messTakeoutOrderDetailRepository.findOrderCount(stock, Arrays.asList(TakeoutStatus.UNPAID, TakeoutStatus.PAID));
            if(count != 0){
                throw new BizException("", "商品：" + stock.getMessProduct().getName() + "，存在未完成订单无法删除。");
            }
            this.shoppingCartProductRepository.deleteAllByMessProduct(stock);
        }

        for (Long id : ids) {
            this.messTakeoutProductStockRepository.deleteById(id);
        }
    }
}
