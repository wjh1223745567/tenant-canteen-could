package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.tackout.MessTakeoutProductTypeAddReq;
import com.iotinall.canteen.dto.tackout.MessTakeoutProductTypeDTO;
import com.iotinall.canteen.dto.tackout.MessTakeoutProductTypeEditReq;
import com.iotinall.canteen.entity.MessTakeoutProductType;
import com.iotinall.canteen.repository.MessTakeoutProductStockRepository;
import com.iotinall.canteen.repository.MessTakeoutProductTypeRepository;
import com.iotinall.canteen.utils.TenantOrgUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class MessTakeoutProductTypeService {
    @Resource
    private MessTakeoutProductTypeRepository messTakeoutProductTypeRepository;

    @Resource
    private MessTakeoutProductStockRepository messTakeoutProductStockRepository;

    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    public Object tree() {
        Long menuId = TenantOrgUtil.findMenuId();
        List<MessTakeoutProductType> result = this.messTakeoutProductTypeRepository.findTree(menuId);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return result.stream().map(
                this::convertTree).collect(Collectors.toList());
    }

    public MessTakeoutProductTypeDTO convertTree(MessTakeoutProductType productType) {
        MessTakeoutProductTypeDTO treeDTO = new MessTakeoutProductTypeDTO();
        treeDTO.setId(productType.getId())
                .setName(productType.getName())
                .setParentId(productType.getParent() != null ? productType.getParent().getId() : null)
                .setRemark(productType.getRemark());
        List<MessTakeoutProductType> messTakeoutProductTypes = this.messTakeoutProductTypeRepository.findAllByParent(productType);
        treeDTO.setChildren(messTakeoutProductTypes.stream().map(this::convertTree).collect(Collectors.toList()));
        return treeDTO;
    }

    public Long create(MessTakeoutProductTypeAddReq req) {
        List<MessTakeoutProductType> productTypes = messTakeoutProductTypeRepository.findByNameAndTenantId(req.getName(), TenantOrgUtil.findMenuId());
        if (!CollectionUtils.isEmpty(productTypes)) {
            throw new BizException("", "商品类型已存在");
        }
        MessTakeoutProductType parent = null;
        if (req.getParentId() != null) {
            parent = new MessTakeoutProductType();
            parent.setId(req.getParentId());
        }
        MessTakeoutProductType messTakeoutProductType = new MessTakeoutProductType()
                .setName(req.getName())
                .setParent(parent)
                .setDeleted(Boolean.FALSE);
        messTakeoutProductType.setRemark(req.getRemark());

        //添加租户信息
        messTakeoutProductType.setTenantId(TenantOrgUtil.findMenuId());

        this.messTakeoutProductTypeRepository.save(messTakeoutProductType);
        return messTakeoutProductType.getId();
    }

    public void update(MessTakeoutProductTypeEditReq req) {
        Optional<MessTakeoutProductType> productTypeOptional = messTakeoutProductTypeRepository.findById(req.getId());
        if (!productTypeOptional.isPresent()) {
            throw new BizException("", "商品类型不存在");
        }

        MessTakeoutProductType productType = productTypeOptional.get();

        if (req.getParentId() != null) {
            this.validPid(req.getId(), req.getParentId());
        }

        List<MessTakeoutProductType> productTypes = messTakeoutProductTypeRepository.findByNameAndTenantId(req.getName(), TenantOrgUtil.findMenuId());
        if (!CollectionUtils.isEmpty(productTypes)) {
            MessTakeoutProductType messTakeoutProductType = productTypes.stream().filter(item ->
                    !item.getId().equals(req.getId())).findAny().orElse(null);
            if (null != messTakeoutProductType) {
                throw new BizException("", "商品类型已存在");
            }
        }

        MessTakeoutProductType parent = null;
        if (req.getParentId() != null) {
            parent = new MessTakeoutProductType();
            parent.setId(req.getParentId());
        }

        productType.setName(req.getName())
                .setParent(parent);

        productType.setId(req.getId())
                .setRemark(req.getRemark());

        messTakeoutProductTypeRepository.save(productType);
    }

    public void validPid(Long thisId, Long pid) {
        List<Long> allChildren = new ArrayList<>();
        allChildren.add(thisId);
        this.findAllChildren(allChildren, thisId);
        if (allChildren.contains(pid)) {
            throw new BizException("", "不能选择当前类别或当前类别子类别为父级节点");
        }
    }

    /**
     * 查询所有子节点
     *
     * @param ids
     * @param id
     */
    public void findAllChildren(List<Long> ids, Long id) {
        MessTakeoutProductType productType = new MessTakeoutProductType();
        productType.setId(id);

        List<MessTakeoutProductType> messTakeoutProductTypes = this.messTakeoutProductTypeRepository.findAllByParent(productType);
        if (!CollectionUtils.isEmpty(messTakeoutProductTypes)) {
            ids.addAll(messTakeoutProductTypes.stream().map(MessTakeoutProductType::getId).collect(Collectors.toList()));
            for (MessTakeoutProductType messTakeoutProductType : messTakeoutProductTypes) {
                this.findAllChildren(ids, messTakeoutProductType.getId());
            }
        }
    }

    public void delete(Long id) {
        Integer count = messTakeoutProductStockRepository.countByProductTypeId(id);
        if (count != 0) {
            throw new BizException("", "当前分类有商品无法删除");
        }
        messTakeoutProductTypeRepository.deleteById(id);
    }

}
