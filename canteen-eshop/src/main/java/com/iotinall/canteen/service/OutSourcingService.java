package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;

import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constants.TackOutPayType;
import com.iotinall.canteen.constants.TakeoutStatus;
import com.iotinall.canteen.dto.apptackout.OutSourcingCondition;
import com.iotinall.canteen.dto.apptackout.OutSourcingDto;
import com.iotinall.canteen.dto.apptackout.OutSourcingProductDto;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.dto.tackout.TackOutPayProductReq;
import com.iotinall.canteen.dto.tackout.TackOutPayReq;
import com.iotinall.canteen.dto.tackout.TackoutInvestMoneyDto;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignMessProductCookView;
import com.iotinall.canteen.dto.organization.FeignSerMoneybagReq;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/2214:35
 */
@Service
public class OutSourcingService {

    @Resource
    private MessTakeoutOrderRepository messTakeoutOrderRepository;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    @Resource
    private MessTakeoutProductStockRepository messTakeoutProductStockRepository;

    @Resource
    private ShoppingCartRepository shoppingCartRepository;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Resource
    private MessTakeoutOrderDetailRepository messTakeoutOrderDetailRepository;

    @Resource
    private FeignSerMoneybagService feignSerMoneybagService;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    public CursorPageDTO<OutSourcingDto> findByPage(OutSourcingCondition condition, Long empid) {
        SpecificationBuilder spec = SpecificationBuilder.builder()
                .where(Criterion.in("sourcingStatus", TakeoutStatus.getByCode(condition.getSourcingStatus()) == TakeoutStatus.PAID ? Arrays.asList(TakeoutStatus.PAID, TakeoutStatus.UNPAID) : TakeoutStatus.getByCode(condition.getSourcingStatus()) != null ? Collections.singleton(TakeoutStatus.getByCode(condition.getSourcingStatus())) : null))
                .where(Criterion.lt("createTime", condition.getCursor() != null ? LocalDateTime.ofInstant(Instant.ofEpochMilli(condition.getCursor()), ZoneOffset.of("+8")) : null))
                .where(Criterion.eq("employeeId", empid))
                .where(
                        Criterion.eq("serialNumber", this.parseInteger(condition.getKey()))
                );

        if (empid == null) {
            Long menuId = feignTenantOrganizationService.findMenuId();
            spec.where(Criterion.eq("tenantId", menuId));
        }

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<MessTakeoutOrder> page = this.messTakeoutOrderRepository.findAll(spec.build(), pageRequest);
        Map<Long, FeignEmployeeDTO> map = feignEmployeeService.findByIds(page.get().map(MessTakeoutOrder::getEmployeeId).collect(Collectors.toSet()));
        List<OutSourcingDto> outSourcingDtos = page.get().map(item -> {
            OutSourcingDto outSourcingDto = new OutSourcingDto()
                    .setId(item.getId())
                    .setTakeoutStatus(item.getSourcingStatus())
                    .setCreateTime(item.getCreateTime())
                    .setOrderNumber(item.getOrderNumber())
                    .setPayAmount(item.getPayAmount())
                    .setSerialNumber(String.format("%04d", item.getSerialNumber()))
                    .setPayTime(item.getPayTime());
            if (item.getEmployeeId() != null && map.containsKey(item.getEmployeeId())) {
                FeignEmployeeDTO feignEmployeeDTO = map.get(item.getEmployeeId());
                outSourcingDto.setEmpName(feignEmployeeDTO.getName());
            }
            List<OutSourcingProductDto> products = item.getMessTakeoutOrderDetails().stream().map(product -> {
                MessTakeoutProductInfo messProduct = product.getMessProduct().getMessProduct();
                return new OutSourcingProductDto()
                        .setName(messProduct.getName())
                        .setImage(messProduct.getImg())
                        .setPrice(product.getBuyAmount())
                        .setCount(product.getCount());
            }).collect(Collectors.toList());
            outSourcingDto.setAllCount(products.stream().mapToInt(OutSourcingProductDto::getCount).sum());
            outSourcingDto.setProducts(products);
            return outSourcingDto;
        }).collect(Collectors.toList());

        return PageUtil.toCursorPageDTO(outSourcingDtos, CollectionUtils.isEmpty(outSourcingDtos) ? -1 : outSourcingDtos.get(outSourcingDtos.size() - 1).getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    /**
     * @return
     * @Author JoeLau
     * @Description H5管理员查看商城订单
     * @Date 2021/7/30  9:26
     * @Param
     */
    public CursorPageDTO<OutSourcingDto> findAllByPage(OutSourcingCondition condition) {
        Long menuId = feignTenantOrganizationService.findMenuId();
        SpecificationBuilder spec = SpecificationBuilder.builder()
                .where(Criterion.in("sourcingStatus", TakeoutStatus.getByCode(condition.getSourcingStatus()) == TakeoutStatus.PAID ? Arrays.asList(TakeoutStatus.PAID, TakeoutStatus.UNPAID) : TakeoutStatus.getByCode(condition.getSourcingStatus()) != null ? Collections.singleton(TakeoutStatus.getByCode(condition.getSourcingStatus())) : null))
                .where(Criterion.lt("createTime", condition.getCursor() != null ? LocalDateTime.ofInstant(Instant.ofEpochMilli(condition.getCursor()), ZoneOffset.of("+8")) : null))
                .where(Criterion.eq("serialNumber", this.parseInteger(condition.getKey())))
                .where(Criterion.eq("tenantId", menuId));

        if (StringUtils.isNotBlank(condition.getKey())) {
                spec.where(Criterion.like("employeeName",condition.getKey()));
        }
        if (null != condition.getCreateTime()) {
            spec.where(Criterion.gte("createTime", LocalDateTimeUtil.str2LocalDateTime(condition.getCreateTime() + " 00:00:00")));
            spec.where(Criterion.lt("createTime", LocalDateTimeUtil.str2LocalDateTime(condition.getCreateTime().plusDays(1) + " 00:00:00")));
        }
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<MessTakeoutOrder> page = this.messTakeoutOrderRepository.findAll(spec.build(), pageRequest);
        Map<Long, FeignEmployeeDTO> map = feignEmployeeService.findByIds(page.get().map(MessTakeoutOrder::getEmployeeId).collect(Collectors.toSet()));
        List<OutSourcingDto> outSourcingDtos = page.get().map(item -> {
            OutSourcingDto outSourcingDto = new OutSourcingDto()
                    .setId(item.getId())
                    .setTakeoutStatus(item.getSourcingStatus())
                    .setCreateTime(item.getCreateTime())
                    .setOrderNumber(item.getOrderNumber())
                    .setPayAmount(item.getPayAmount())
                    .setSerialNumber(String.format("%04d", item.getSerialNumber()))
                    .setPayTime(item.getPayTime());
            if (item.getEmployeeId() != null && map.containsKey(item.getEmployeeId())) {
                FeignEmployeeDTO feignEmployeeDTO = map.get(item.getEmployeeId());
                outSourcingDto.setEmpName(feignEmployeeDTO.getName());
            }
            List<OutSourcingProductDto> products = item.getMessTakeoutOrderDetails().stream().map(product -> {
                MessTakeoutProductInfo messProduct = product.getMessProduct().getMessProduct();
                return new OutSourcingProductDto()
                        .setName(messProduct.getName())
                        .setImage(messProduct.getImg())
                        .setPrice(product.getBuyAmount())
                        .setCount(product.getCount());
            }).collect(Collectors.toList());
            outSourcingDto.setAllCount(products.stream().mapToInt(OutSourcingProductDto::getCount).sum());
            outSourcingDto.setProducts(products);
            return outSourcingDto;
        }).collect(Collectors.toList());

        return PageUtil.toCursorPageDTO(outSourcingDtos, CollectionUtils.isEmpty(outSourcingDtos) ? -1 : outSourcingDtos.get(outSourcingDtos.size() - 1).getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    private Integer parseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOutSourcingById(Long id) {
        MessTakeoutOrder messTakeoutOrder = this.messTakeoutOrderRepository.findById(id).orElseThrow(() -> new BizException("", "未找到订单信息"));
        LocalDateTime orderTime = messTakeoutOrder.getCreateTime();

        //周三 18:00
        LocalDateTime shoppingDeadlineTime = LocalDateTime.of(LocalDate.now().with(DayOfWeek.WEDNESDAY), LocalTime.of(18, 0));

        /**
         * 0、当前时间在周三18:00之前，本周订单，可以直接取消
         * 1、下周订单随时可以取消
         */
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(shoppingDeadlineTime) && orderTime.isBefore(shoppingDeadlineTime)) {
            throw new BizException("", "本周订单已截止，无法取消订单");
        }

        if (!Objects.equals(messTakeoutOrder.getSourcingStatus().getCode(), TakeoutStatus.PAID.getCode())) {
            throw new BizException("", "订单已经完成或取消");
        }

        if (messTakeoutOrder.getEmployeeId() == null) {
            throw new BizException("", "未找到订单用户");
        }

        Integer code = this.messTakeoutOrderRepository.cancelOrder(id);
        if (code == 0) {
            throw new BizException("", "取消订单失败");
        }

        //TODO
        throw new BizException("", "暂不支持取消订单及退款");
//        FinTransactionRecord finTransactionRecord = finTransactionRecordRepository.findByOrder(messTakeoutOrder);
//        if (null == finTransactionRecord) {
//            throw new BizException("未找到消费记录");
//        }
//
//        //更新消费记录为已取消
//        finTransactionRecord.setState(2);
//        this.finTransactionRecordRepository.save(finTransactionRecord);
//
//        //退钱
//        this.employeeWalletService.addBalance(messTakeoutOrder.getEmployee().getWallet(), messTakeoutOrder.getPayAmount());
//
//        //退货
//        Set<MessTakeoutOrderDetail> messTakeoutOrderDetails = messTakeoutOrder.getMessTakeoutOrderDetails();
//        for (MessTakeoutOrderDetail messTakeoutOrderDetail : messTakeoutOrderDetails) {
//            MessTakeoutProductStock messTakeoutProductStock = messTakeoutOrderDetail.getMessProduct();
//            messTakeoutProductStock.setSalesVolume(messTakeoutOrderDetail.getMessProduct().getSalesVolume() - messTakeoutOrderDetail.getCount());
//            this.messTakeoutProductStockRepository.save(messTakeoutProductStock);
//        }
//
//        //余额变动记录
//        WalletBillDetail walletBillDetail = new WalletBillDetail();
//        walletBillDetail.setWalletId(messTakeoutOrder.getEmployee().getWallet().getId());
//        walletBillDetail.setRecordId(messTakeoutOrder.getId());
//        walletBillDetail.setRecordType(3);
//        walletBillDetail.setCreateTime(LocalDateTime.now());
//        walletBillDetail.setType(1);
//        walletBillDetail.setAmount(messTakeoutOrder.getPayAmount());
//        walletBillDetail.setReason(Constants.WALLET_BILL_DETAIL_CONSUME_REFUND);
//        this.walletBillDetailRepository.save(walletBillDetail);
//
//        //同步到闸机项目
//        this.gatePassDataSyncTaskRecordService.createTask(2, finTransactionRecord.getId());
    }

    public OutSourcingDto findOutSourcingById(Long id) {
        MessTakeoutOrder messTakeoutOrder = this.messTakeoutOrderRepository.findById(id).orElse(null);
        if (messTakeoutOrder == null) {
            throw new BizException("", "未找到订单信息");
        }


        OutSourcingDto outSourcingDto = new OutSourcingDto()
                .setId(messTakeoutOrder.getId())
                .setTakeoutStatus(messTakeoutOrder.getSourcingStatus())
                .setCreateTime(messTakeoutOrder.getCreateTime())
                .setTackOutPayType(messTakeoutOrder.getTackOutPayType())
                .setOrderNumber(messTakeoutOrder.getOrderNumber())
                .setPayAmount(messTakeoutOrder.getPayAmount())
                .setSerialNumber(String.format("%04d", messTakeoutOrder.getSerialNumber()))
                .setPayTime(messTakeoutOrder.getPayTime());

        FeignMessProductCookView feignEmployeeDTO = this.feignEmployeeService.findCookView(messTakeoutOrder.getEmployeeId());
        if (feignEmployeeDTO != null) {
            outSourcingDto.setEmpName(feignEmployeeDTO.getName());
            outSourcingDto.setPhone(feignEmployeeDTO.getMobile());
        }

        List<OutSourcingProductDto> products = messTakeoutOrder.getMessTakeoutOrderDetails().stream().map(product -> {
            MessTakeoutProductInfo messProduct = product.getMessProduct().getMessProduct();
            return new OutSourcingProductDto()
                    .setName(messProduct.getName())
                    .setImage(messProduct.getImg())
                    .setPrice(product.getMessProduct().getPrice())
                    .setCount(product.getCount());
        }).collect(Collectors.toList());
        outSourcingDto.setProducts(products);
        return outSourcingDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void pickup(Long id) {
        MessTakeoutOrder messTakeoutOrder = messTakeoutOrderRepository.findById(id).orElseThrow(() -> new BizException("", "未找到订单信息"));
        LocalDateTime orderTime = messTakeoutOrder.getCreateTime();
        LocalDateTime shoppingDeadlineTime = LocalDateTime.of(LocalDate.now().with(DayOfWeek.WEDNESDAY), LocalTime.of(18, 0));

        //判断是否为这周订单
        if (orderTime.isAfter(shoppingDeadlineTime)) {
            //下周订单
            throw new BizException("", "订单为下周的订单，只能下周发货");
        }

        //本周订单
        Integer count = this.messTakeoutOrderRepository.pickup(id, TakeoutStatus.COMPLETED);
        if (count != 1) {
            throw new BizException("", "发货失败");
        }
    }

    /**
     * 外带外购钱包支付
     *
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.DASHBOARD_CONSUME_STATIS, RedisCacheUtil.dashboardEatAllCount, RedisCacheUtil.FIN_TRANSACTION_RECORD, RedisCacheUtil.TAKEOUT_ORDER, RedisCacheUtil.tackOutMessProduct, RedisCacheUtil.tackOutOutsourcing, RedisCacheUtil.MESS_TAKEOUT_PRODUCT, RedisCacheUtil.appShoppingCart}, allEntries = true)
    public TackoutInvestMoneyDto tackOutWxPay(TackOutPayReq req, Boolean immediately) {
        Long empId = SecurityUtils.getUserId();
        if (req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("", "支付金额不能小于等于零");
        }

        Long tenantId = feignTenantOrganizationService.findMenuId();

        MessTakeoutOrder order = new MessTakeoutOrder()
                .setSourcingStatus(TakeoutStatus.UNPAID)
                .setEmployeeId(empId)
                .setEmployeeName(this.feignEmployeeService.findCookView(empId).getName())
                .setTackOutPayType(TackOutPayType.getByCode(req.getTackOutPayType()))
                .setOrderNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 16))
                .setPayAmount(BigDecimal.ZERO)
                .setTenantId(tenantId)
                .setSerialNumber(RandomUtils.nextInt(1000, 100000));
        order = this.messTakeoutOrderRepository.save(order);

        Set<MessTakeoutOrderDetail> orderDetailList = new HashSet<>();
        MessTakeoutProductStock messTackOutProduct;
        for (TackOutPayProductReq item : req.getProducts()) {
            messTackOutProduct = this.messTakeoutProductStockRepository.findById(item.getId()).orElse(null);
            if (messTackOutProduct == null || !messTackOutProduct.getState()) {
                throw new BizException("", "商品已下架");
            }

            if ((messTackOutProduct.getCount() - messTackOutProduct.getSalesVolume()) <= 0) {
                throw new BizException("", "商品已售空：" + messTackOutProduct.getMessProduct().getName());
            }

            if (messTackOutProduct.getCount() - messTackOutProduct.getSalesVolume() < item.getCount()) {
                throw new BizException("", "商品库存不足：" + messTackOutProduct.getMessProduct().getName());
            }

            //限购
            ShoppingCart shoppingCart = this.shoppingCartRepository.findByOrgEmployeeId(empId);
            ShoppingCartProduct shoppingCartProduct = this.shoppingCartProductRepository.findFirstByShoppingCartAndMessProduct_Id(shoppingCart, messTackOutProduct.getId());
            this.shoppingCartService.limitCount(empId, messTackOutProduct, shoppingCartProduct != null ? shoppingCartProduct.getCount() : 0);

            //清空购物车
            if (shoppingCartProduct != null && !immediately) {
                this.shoppingCartProductRepository.delete(shoppingCartProduct);
            }

            //保存已售数量
            messTackOutProduct.setSalesVolume(messTackOutProduct.getSalesVolume() != null ? messTackOutProduct.getSalesVolume() + item.getCount() : item.getCount());
            this.messTakeoutProductStockRepository.save(messTackOutProduct);

            //订单明细
            MessTakeoutOrderDetail messTakeoutOrderDetail = new MessTakeoutOrderDetail()
                    .setMessTakeoutOrder(order)
                    .setMessProduct(messTackOutProduct)
                    .setBuyAmount(messTackOutProduct.getPrice())
                    .setCount(item.getCount());
            orderDetailList.add(messTakeoutOrderDetail);

            //计算总价
            order.setPayAmount(order.getPayAmount().add(messTackOutProduct.getPrice().multiply(BigDecimal.valueOf(item.getCount()))));
        }

        if (req.getAmount().compareTo(order.getPayAmount()) != 0) {
            throw new BizException("", "订单异常请重新下单");
        }

        this.messTakeoutOrderDetailRepository.saveAll(orderDetailList);

        order.setMessTakeoutOrderDetails(orderDetailList);
        this.messTakeoutOrderRepository.save(order);

        switch (TackOutPayType.getByCode(req.getTackOutPayType())) {
            case WXPAY:
                return null;
            case SERMONEYBAG:
                TackoutInvestMoneyDto bagPay = new TackoutInvestMoneyDto()
                        .setTackOutPayType(TackOutPayType.SERMONEYBAG)
                        .setOrderId(order.getId());

                order.setPayTime(LocalDateTime.now());
                order.setSourcingStatus(TakeoutStatus.PAID);
                this.messTakeoutOrderRepository.save(order);
                FeignSerMoneybagReq serMoneybagReq = new FeignSerMoneybagReq()
                        .setAmount(order.getPayAmount())
                        .setPassword(req.getPayPassword())
                        .setTackOutOrderId(order.getId())
                        .setOrderCreateTime(order.getPayTime());

                this.feignSerMoneybagService.serMoneybagPay(serMoneybagReq);

                return bagPay;
            case ONSITEPAYMENT:
                return new TackoutInvestMoneyDto()
                        .setTackOutPayType(TackOutPayType.ONSITEPAYMENT)
                        .setOrderId(order.getId());
            default: {
                throw new BizException("", "支付失败，未找到支付方式");
            }
        }
    }
}
