package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.suppliertype.StockSupplierTypeDTO;
import com.iotinall.canteen.dto.suppliertype.StockSupplierV2TypeAddReq;
import com.iotinall.canteen.dto.suppliertype.StockSupplierV2TypeEditReq;
import com.iotinall.canteen.entity.StockSupplierType;
import com.iotinall.canteen.repository.StockSupplierRepository;
import com.iotinall.canteen.repository.StockSupplierTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商类型逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockSupplierTypeService {
    @Resource
    private StockSupplierTypeRepository stockSupplierTypeRepository;
    @Resource
    private StockSupplierRepository stockSupplierRepository;


    /**
     * 分页
     *
     * @author loki
     * @date 2021/06/03 14:46
     */
    public PageDTO<StockSupplierTypeDTO> page(Pageable pageable) {
        Page<StockSupplierType> page = this.stockSupplierTypeRepository.findAll(pageable);
        List<StockSupplierTypeDTO> supplierV2TypeDTOS = page.get().map(item -> {
            StockSupplierTypeDTO supplierV2TypeDTO = new StockSupplierTypeDTO()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setRemark(item.getRemark());
            return supplierV2TypeDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(supplierV2TypeDTOS, page);
    }

    /**
     * 添加
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(StockSupplierV2TypeAddReq req) {
        StockSupplierType type = this.stockSupplierTypeRepository.findByName(req.getName());
        if (null != type) {
            throw new BizException(" ", "类型名称已存在");
        }

        type = new StockSupplierType();
        type.setName(req.getName());
        type.setRemark(req.getRemark());
        this.stockSupplierTypeRepository.save(type);
    }

    /**
     * 修改
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(StockSupplierV2TypeEditReq req) {
        StockSupplierType supplierType = this.stockSupplierTypeRepository.findById(req.getId()).orElseThrow(
                () -> new BizException("供应商类型不存在")
        );

        StockSupplierType supplierType2 = this.stockSupplierTypeRepository.findByName(req.getName());
        if (null != supplierType2 && !supplierType2.getId().equals(req.getId())) {
            throw new BizException(" ", "供应商类型名称已存在，编辑失败");
        }

        supplierType.setName(req.getName());
        supplierType.setRemark(req.getRemark());
        this.stockSupplierTypeRepository.save(supplierType);
    }

    /**
     * 批量删除
     *
     * @author loki
     * @date 2021/06/03 14:54
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的供应商类型");
        }

        Long count;
        StockSupplierType type;
        for (Long id : ids) {
            type = this.stockSupplierTypeRepository.findById(id).orElseThrow(
                    () -> new BizException("供应商类型不存在")
            );

            //校验供应商类型是否被使用
            count = this.stockSupplierRepository.countByType(type);
            if (null != count && count > 0) {
                throw new BizException("", "供应商类型【" + type.getName() + "】已被使用，不能删除");
            }
            this.stockSupplierTypeRepository.delete(type);
        }
    }
}
