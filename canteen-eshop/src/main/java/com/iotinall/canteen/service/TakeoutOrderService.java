package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constants.TakeoutStatus;
import com.iotinall.canteen.dto.sourcing.OutSourcingCollectDto;
import com.iotinall.canteen.dto.sourcing.OutSourcingCondition;
import com.iotinall.canteen.dto.sourcing.OutSourcingDto;
import com.iotinall.canteen.dto.sourcing.OutSourcingProductDto;
import com.iotinall.canteen.entity.MessTakeoutOrder;
import com.iotinall.canteen.entity.MessTakeoutOrderDetail;
import com.iotinall.canteen.entity.MessTakeoutProductInfo;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.MessTakeoutOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/11/2214:35
 */
@Service
public class TakeoutOrderService {

    @Resource
    private MessTakeoutOrderRepository messTakeoutOrderRepository;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    public PageDTO<OutSourcingDto> findByPage(OutSourcingCondition condition, Pageable pageable) {
        Specification<MessTakeoutOrder> spec = SpecificationBuilder.builder()
                .where(Criterion.like("employee.name", condition.getEmpName()))
                .where(Criterion.in("sourcingStatus", TakeoutStatus.getByCode(condition.getTakeoutStatus()) == TakeoutStatus.PAID ? Arrays.asList(TakeoutStatus.PAID, TakeoutStatus.UNPAID) : TakeoutStatus.getByCode(condition.getTakeoutStatus()) != null ? Collections.singleton(TakeoutStatus.getByCode(condition.getTakeoutStatus())) : null))
                .where(
                        Criterion.gte("createTime", condition.getCreateTime() == null ? null : condition.getCreateTime().atStartOfDay()),
                        Criterion.lte("createTime", condition.getCreateTime() == null ? null : condition.getCreateTime().atTime(LocalTime.MAX))
                )
                .where(Criterion.eq("tenantId", feignTenantOrganizationService.findMenuId()))
                .build();

        Page<MessTakeoutOrder> page = this.messTakeoutOrderRepository.findAll(spec, pageable);
        Set<Long> empIds = page.get().map(MessTakeoutOrder::getEmployeeId).collect(Collectors.toSet());
        Map<Long, FeignEmployeeDTO> map = feignEmployeeService.findByIds(empIds);
        List<OutSourcingDto> outSourcingDtos = page.get().map(item -> {
            OutSourcingDto outSourcingDto = new OutSourcingDto()
                    .setId(item.getId())
                    .setTakeoutStatus(item.getSourcingStatus())
                    .setCreateTime(item.getCreateTime())
                    .setOrderNumber(item.getOrderNumber())
                    .setTackOutPayType(item.getTackOutPayType())

                    .setPayAmount(item.getPayAmount())
                    .setSerialNumber(String.format("%04d", item.getSerialNumber()))
                    .setPayTime(item.getPayTime());
            if(map.containsKey(item.getEmployeeId())){
                FeignEmployeeDTO feignEmployeeDTO = map.get(item.getEmployeeId());
                outSourcingDto.setEmpName(feignEmployeeDTO != null ? feignEmployeeDTO.getName() : null);
                outSourcingDto.setOrgName(feignEmployeeDTO != null ? feignEmployeeDTO.getOrgName() : null);
            }
            List<OutSourcingProductDto> products = item.getMessTakeoutOrderDetails().stream().map(product -> {
                MessTakeoutProductInfo messProduct = product.getMessProduct().getMessProduct();
                return new OutSourcingProductDto()
                        .setName(messProduct.getName())
                        .setImage(messProduct.getImg())
                        .setCount(product.getCount());
            }).collect(Collectors.toList());
            outSourcingDto.setProducts(products);
            return outSourcingDto;
        }).collect(Collectors.toList());

        return PageUtil.toPageDTO(outSourcingDtos, page);
    }

    /**
     * 本周订单汇总
     */
    public List<OutSourcingCollectDto> printCollect(){
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now().with(DayOfWeek.WEDNESDAY), LocalTime.of(18,0));
        LocalDateTime startDay = endTime.minusDays(7);

        Specification<MessTakeoutOrder> spec = SpecificationBuilder.builder()
                .where(
                    Criterion.eq("sourcingStatus", TakeoutStatus.PAID.getCode()),
                    Criterion.gte("createTime", startDay),
                    Criterion.lte("createTime", endTime)
                ).build();

        List<MessTakeoutOrder> messTakeoutOrders = this.messTakeoutOrderRepository.findAll(spec);

        List<OutSourcingCollectDto> outSourcingCollectDtos = new ArrayList<>();

        for (MessTakeoutOrder messTakeoutOrder : messTakeoutOrders) {

            for (MessTakeoutOrderDetail messTakeoutOrderDetail : messTakeoutOrder.getMessTakeoutOrderDetails()) {
                if(messTakeoutOrderDetail.getMessProduct() == null || messTakeoutOrderDetail.getMessProduct().getMessProduct() == null){
                    continue;
                }
                Optional<OutSourcingCollectDto> collectDtoOptional = outSourcingCollectDtos.stream().filter(item -> Objects.equals(messTakeoutOrderDetail.getMessProduct().getMessProduct().getId(), item.getProdId())).findFirst();
                if(collectDtoOptional.isPresent()){
                    OutSourcingCollectDto collectDto = collectDtoOptional.get();
                    collectDto.setCount((collectDto.getCount() != null ? collectDto.getCount() : 0) + (messTakeoutOrderDetail.getCount() != null ? messTakeoutOrderDetail.getCount() : 0));
                }else{
                    MessTakeoutProductInfo productInfo = messTakeoutOrderDetail.getMessProduct().getMessProduct();
                    OutSourcingCollectDto collectDto = new OutSourcingCollectDto()
                            .setProdId(productInfo.getId())
                            .setName(productInfo.getName())
                            .setSpecification(productInfo.getSpecificationModel())
                            .setCount(messTakeoutOrderDetail.getCount());
                    outSourcingCollectDtos.add(collectDto);
                }
            }
        }

        return outSourcingCollectDtos;
    }

    @Transactional(rollbackOn = Exception.class)
    public void pickup(Long id) {
        MessTakeoutOrder messTakeoutOrder = messTakeoutOrderRepository.findById(id).orElseThrow(() -> new BizException("", "未找到订单信息"));
        LocalDateTime orderTime = messTakeoutOrder.getCreateTime();
        LocalDateTime shoppingDeadlineTime = LocalDateTime.of(LocalDate.now().with(DayOfWeek.WEDNESDAY), LocalTime.of(18, 0));

        //判断是否为这周订单
        if (orderTime.isAfter(shoppingDeadlineTime)) {
            //下周订单
            throw new BizException("", "订单为下周的订单，只能下周发货");
        }

        Integer count = messTakeoutOrderRepository.pickup(id, TakeoutStatus.COMPLETED);
        if(count != 1){
            throw new BizException("", "发货失败");
        }
    }
}
