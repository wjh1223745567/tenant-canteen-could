package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.dto.information.InformationAddReq;
import com.iotinall.canteen.dto.information.InformationCondition;
import com.iotinall.canteen.dto.information.InformationDTO;
import com.iotinall.canteen.dto.information.InformationEditReq;
import com.iotinall.canteen.entity.Information;
import com.iotinall.canteen.entity.InformationType;
import com.iotinall.canteen.repository.InformationRepository;
import com.iotinall.canteen.repository.InformationTypeRepository;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资讯逻辑处理类
 *
 * @author WJH
 * @date 2019/11/110:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysInformationService {
    @Resource
    private InformationRepository informationRepository;
    @Resource
    private InformationTypeRepository informationTypeRepository;

    @Resource
    private FeignOrgService feignOrgService;

    public PageDTO<InformationDTO> findByPage(InformationCondition condition, Pageable pageable) {
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "sticky")
                .and(Sort.by(Sort.Direction.ASC, "seq"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Information> page = this.informationRepository.findAll((Specification<Information>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (condition.getTypeId() != null) {
                predicate = criteriaBuilder.equal(root.get("type").get("id"), condition.getTypeId());
            }
            if (!StringUtils.isBlank(condition.getTitle())) {
                String fuzzy = "%" + condition.getTitle() + "%";
                Predicate title = criteriaBuilder.like(root.get("title"), fuzzy);
                predicate = criteriaBuilder.and(predicate, title);
            }
            if (condition.getStartTime() != null) {
                Predicate predicate1 = criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), condition.getStartTime());
                predicate = criteriaBuilder.and(predicate, predicate1);
            }
            if (condition.getEndTime() != null) {
                Predicate predicate1 = criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), condition.getEndTime().plusDays(1));
                predicate = criteriaBuilder.and(predicate, predicate1);
            }
            return predicate;
        }, pageable);
        List<InformationDTO> informationDTOS = page.get().map(this::copyData).collect(Collectors.toList());
        return PageUtil.toPageDTO(informationDTOS, page);
    }

    private InformationDTO copyData(Information information) {
        InformationDTO dto = new InformationDTO()
                .setId(information.getId())
                .setContent(information.getContent())
                .setTitle(information.getTitle())
                .setCover(information.getCover())
                .setPraiseCount(information.getPraiseCount())
                .setStatus(information.getStatus())
                .setSticky(information.getSticky())
                .setSeq(information.getSeq())
                .setCreateTime(information.getCreateTime())
                .setEdit(SecurityUtils.getCurrentUser().isSupperAdmin() || null == information.getPublisherOrgId() || (SecurityUtils.getCurrentUser().getOrgId() != null && information.getPublisherOrgId().longValue() == SecurityUtils.getCurrentUser().getOrgId()))
                .setUpdateTime(information.getUpdateTime());
        dto.setType(
                dto.new Type().setId(information.getType() != null ? information.getType().getId() : null)
                        .setName(information.getType() != null ? information.getType().getName() : null)
        );

        return dto;
    }

    public Object add(InformationAddReq req) {
        InformationType type = this.informationTypeRepository.findById(req.getTypeId()).orElse(null);
        if (type == null) {
            throw new BizException("", "资讯类型不存在");
        }

        Information information = new Information();
        information.setTitle(req.getTitle())
                .setContent(req.getContent())
                .setCover(req.getCover())
                .setSticky(req.getSticky())
                .setStatus(req.getStatus())
                .setSeq(req.getSeq())
                .setPraiseCount(0)
                .setType(type);

        //资讯接收对象
        Set<Long> orgIds = new HashSet<>(0);
        if(!CollectionUtils.isEmpty(req.getReceivers())){
            orgIds = feignOrgService.getAllChildOrg(req.getReceivers());
        }
        information.setReceiver(CollectionUtils.isEmpty(orgIds) ? "" : orgIds.stream().map(item -> item + "").collect(Collectors.joining(",")));

        //资讯发布人组织机构
        information.setPublisherOrgId(SecurityUtils.getCurrentUser().getOrgId());

        this.informationTypeRepository.addInfoCount(type.getId(), 1);
        this.informationRepository.save(information);
//        this.sendMessageService.informationNotice(information);

        return information;
    }

    public Object edit(InformationEditReq req) {
        Information information = informationRepository.findById(req.getId()).orElse(null);
        if (information == null) {
            throw new BizException("", "修改的记录不存在");
        }
        InformationType type = this.informationTypeRepository.findById(req.getTypeId()).orElse(null);
        if (type == null) {
            throw new BizException("", "资讯类型不存在");
        }
        information.setTitle(req.getTitle())
                .setContent(req.getContent())
                .setCover(req.getCover())
                .setType(type)
                .setSeq(req.getSeq())
                .setSticky(req.getSticky())
                .setStatus(req.getStatus())
                .setUpdateTime(LocalDateTime.now());

        //资讯接收对象
        Set<Long> orgIds = new HashSet<>(0);
        if(!CollectionUtils.isEmpty(req.getReceivers())){
            orgIds = feignOrgService.getAllChildOrg(req.getReceivers());
        }
        information.setReceiver(CollectionUtils.isEmpty(orgIds) ? "" : orgIds.stream().map(item -> item + "").collect(Collectors.joining(",")));

        //资讯发布人组织机构
        information.setPublisherOrgId(SecurityUtils.getCurrentUser().getOrgId());

        return informationRepository.save(information);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object stickyInfo(Long id) {
        Optional<Information> optionalInformation = this.informationRepository.findById(id);
        if (!optionalInformation.isPresent()) {
            throw new BizException("", "未找到当前咨询信息");
        }
        Information info = optionalInformation.get();
        info.setSticky(info.getSticky() != null ? !info.getSticky() : Boolean.FALSE);
        return this.informationRepository.save(info);
    }

    public Object deleted(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择删除的项");
        }

        List<Information> informationList = new ArrayList<>();
        Information information;
        for (Long id : ids) {
            information = informationRepository.findById(id).orElseThrow(() -> new BizException("", "资讯不存在"));
            informationList.add(information);

            informationRepository.delete(information);
            informationTypeRepository.addInfoCount(information.getType().getId(), -1);
        }

        return informationList;
    }
}