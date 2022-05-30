package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.rule.RuleAddReq;
import com.iotinall.canteen.dto.rule.RuleDTO;
import com.iotinall.canteen.dto.rule.RuleEditReq;
import com.iotinall.canteen.dto.rule.RuleListReq;
import com.iotinall.canteen.entity.KitchenItem;
import com.iotinall.canteen.entity.KitchenRule;
import com.iotinall.canteen.repository.KitchenItemRepository;
import com.iotinall.canteen.repository.KitchenRuleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 规章制度service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KitchenRuleService {
    @Resource
    private KitchenRuleRepository kitchenRuleRepository;
    @Resource
    private KitchenItemRepository kitchenItemRepository;

    public PageDTO<RuleDTO> list(RuleListReq req, Pageable pageable) {
        Specification<KitchenRule> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("type.id", req.getTypeId()))
                .where(Criterion.eq("enabled", req.getEnabled()))
                .where(Criterion.like("title", req.getKeyword()))
                .build(true);
        Page<KitchenRule> page = kitchenRuleRepository.findAll(specification, pageable);
        List<RuleDTO> collect = page.stream().map(item -> {
            RuleDTO dto = new RuleDTO();
            BeanUtils.copyProperties(item, dto);
            dto.setTypeId(item.getType().getId());
            dto.setTypeName(item.getType().getName());
            //dto.setTypeDesc(item.getType().getDescription());
            dto.setCreateTime(LocalDateTime.now());
            return dto;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(collect, page);
    }

    public void add(RuleAddReq req) {
        KitchenRule byTitle = kitchenRuleRepository.findByTitle(req.getTitle());
        if (byTitle != null) {
            throw new BizException("", "已存在同名的规章制度");
        }
        Optional<KitchenItem> byId = kitchenItemRepository.findById(req.getTypeId());
        if (!byId.isPresent()) {
            throw new BizException("", "规章类型不存在");
        }
        KitchenRule rule = new KitchenRule();
        BeanUtils.copyProperties(req, rule);
        rule.setType(byId.get());
        rule = kitchenRuleRepository.save(rule);
    }

    public void edit(RuleEditReq req) {
        KitchenRule rule = kitchenRuleRepository.findById(req.getId()).orElse(null);
        if (rule == null) {
            throw new BizException("", "修改的记录不存在");
        }

        KitchenRule byTitle = kitchenRuleRepository.findByTitle(req.getTitle());
        if (byTitle != null && !byTitle.getId().equals(rule.getId())) {
            throw new BizException("", "已存在同名的规章制度");
        }

        BeanUtils.copyProperties(req, rule);

        if (rule.getType() != null && !rule.getType().getId().equals(req.getTypeId())) {
            Optional<KitchenItem> byId = kitchenItemRepository.findById(req.getTypeId());
            if (!byId.isPresent()) {
                throw new BizException("", "规章类型不存在");
            }
            rule.setType(byId.get());
        }

        rule = kitchenRuleRepository.save(rule);
    }

    public void del(Long[] batch) {
        if (batch.length == 0) {
            throw new BizException("", "请选择需要删除的记录");
        }

        List<KitchenRule> ruleList = new ArrayList<>();
        KitchenRule rule;
        for (Long id : batch) {
            rule = this.kitchenRuleRepository.findById(id).orElseThrow(() -> new BizException("", "考勤制度不存在"));
            ruleList.add(rule);
        }
        kitchenRuleRepository.deleteAll(ruleList);
    }
}
