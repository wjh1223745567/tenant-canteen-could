package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.supplier.StockSupplierAddReq;
import com.iotinall.canteen.dto.supplier.StockSupplierDTO;
import com.iotinall.canteen.dto.supplier.StockSupplierEditReq;
import com.iotinall.canteen.dto.supplier.StockSupplierV2Req;
import com.iotinall.canteen.entity.StockSupplier;
import com.iotinall.canteen.entity.StockSupplierType;
import com.iotinall.canteen.repository.StockBillRepository;
import com.iotinall.canteen.repository.StockSupplierRepository;
import com.iotinall.canteen.repository.StockSupplierTypeRepository;
import com.iotinall.canteen.utils.CodeGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockSupplierService {
    @Resource
    private StockSupplierRepository stockSupplierRepository;
    @Resource
    private StockSupplierTypeRepository stockSupplierTypeRepository;
    @Resource
    private StockBillRepository stockBillRepository;

    /**
     * 分页
     *
     * @author loki
     * @date 2021/06/03 14:23
     */
    public PageDTO<StockSupplierDTO> page(StockSupplierV2Req req, Pageable pageable) {
        Specification<StockSupplier> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("type.id", req.getTypeId()))
                .where(Criterion.eq("credit", req.getCredit()))
                .whereByOr(
                        Criterion.like("name", req.getName())
                )
                .build();
        Page<StockSupplier> page = this.stockSupplierRepository.findAll(spec, pageable);
        List<StockSupplierDTO> supplierList = page.get().map(item -> new StockSupplierDTO()
                .setName(item.getName())
                .setCode(item.getCode())
                .setId(item.getId())
                .setCredit(item.getCredit())
                .setContact(item.getContact())
                .setContactNumber(item.getContactNumber())
                .setAddress(item.getAddress())
                .setSupplierTypeId(item.getType().getId())
                .setSupplierTypeName(item.getType().getName())
                .setCooperation(item.getCooperation())).collect(Collectors.toList());
        return PageUtil.toPageDTO(supplierList, page);
    }

    /**
     * 添加供应商
     *
     * @author loki
     * @date 2021/06/03 14:29
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(StockSupplierAddReq req) {
        //校验名称是否重复
        StockSupplier supplier = this.stockSupplierRepository.findByName(req.getName());
        if (null != supplier) {
            throw new BizException("", "供应商已存在，添加失败");
        }

        //校验类别是否存在
        StockSupplierType stockSupplierV2Type = this.stockSupplierTypeRepository.findById(req.getSupplierTypeId()).orElse(null);
        if (null == stockSupplierV2Type) {
            throw new BizException("", "供应商类型不存在");
        }

        //保存
        supplier = new StockSupplier();
        supplier.setCode(CodeGeneratorUtil.buildCode(6));
        supplier.setName(req.getName());
        supplier.setType(stockSupplierV2Type);
        supplier.setCredit(req.getCredit());
        supplier.setContact(req.getContact());
        supplier.setContactNumber(req.getContactNumber());
        supplier.setAddress(req.getAddress());
        supplier.setCooperation(req.getCooperation());
        this.stockSupplierRepository.save(supplier);
    }

    /**
     * 编辑
     *
     * @author loki
     * @date 2021/06/03 14:30
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(StockSupplierEditReq req) {
        //名称是否重复
        StockSupplier supplier = this.stockSupplierRepository.findById(req.getId()).orElseThrow(
                () -> new BizException("供应商不存在")
        );

        StockSupplier supplier2 = this.stockSupplierRepository.findByName(req.getName());
        if (null != supplier2 && !supplier2.getId().equals(req.getId())) {
            throw new BizException("", "供应商名称重复，修改失败");
        }

        StockSupplierType supplierType = this.stockSupplierTypeRepository.findById(req.getSupplierTypeId()).orElseThrow(
                () -> new BizException("供应商类型不存在")
        );

        //保存
        BeanUtils.copyProperties(req, supplier);
        supplier.setType(supplierType);

        this.stockSupplierRepository.save(supplier);
    }

    /**
     * 批量删除
     *
     * @author loki
     * @date 2021/06/03 14:40
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的供应商");
        }

        StockSupplier supplier;
        for (Long id : ids) {
            supplier = this.stockSupplierRepository.findById(id).orElseThrow(
                    () -> new BizException("供应商不存在")
            );

            //供应商是否已经被使用
            Long count = this.stockBillRepository.countBySupplier(supplier);
            if (null != count && count > 0) {
                throw new BizException("", "供应商【" + supplier.getName() + "】已被使用，不能删除");
            }
            stockSupplierRepository.delete(supplier);
        }
    }

    /**
     * 获取供应商
     *
     * @author loki
     * @date 2021/6/4 14:53
     **/
    public StockSupplier getSupplierById(Long id) {
        return null == id ?
                null :
                this.stockSupplierRepository.findById(id).orElseThrow(
                        () -> new BizException("供应商不存在")
                );
    }
}
