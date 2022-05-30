package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.information.*;
import com.iotinall.canteen.entity.Information;
import com.iotinall.canteen.entity.InformationComment;
import com.iotinall.canteen.entity.InformationType;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.InformationCommentRepository;
import com.iotinall.canteen.repository.InformationRepository;
import com.iotinall.canteen.repository.InformationTypeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 咨询列表
 *
 * @author WJH
 * @date 2019/11/1116:36
 */
@Service
public class AppInformationService {
    @Resource
    private InformationRepository informationRepository;
    @Resource
    private InformationCommentRepository informationCommentRepository;
    @Resource
    private InformationTypeRepository informationTypeRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    public List<InformationListDTO> listTop() {
        PageRequest pq = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("sticky"), Sort.Order.asc("seq")));
        Page<Information> top5 = informationRepository.findAll(pq);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        List<InformationListDTO> list = top5.get().map(item -> {
            InformationListDTO dto = new InformationListDTO()
                    .setId(item.getId())
                    .setTitle(item.getTitle())
                    .setTypeName(item.getType().getName())
                    .setSticky(item.getSticky())
                    .setCover(item.getCover())
                    .setTime(item.getUpdateTime().format(dateTimeFormatter));
            return dto;
        }).collect(Collectors.toList());
        return list;
    }

    public List<InformationListDTO> listNewest() {
        PageRequest pq = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("sticky"), Sort.Order.asc("seq")));
        Page<Information> newest = this.informationRepository.findAll(pq);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        List<InformationListDTO> list = newest.get().map(item -> {
            InformationListDTO dto = new InformationListDTO()
                    .setId(item.getId())
                    .setTitle(item.getTitle())
                    .setTypeName(item.getType().getName())
                    .setSticky(item.getSticky())
                    .setCover(item.getCover())
                    .setTime(item.getUpdateTime().format(dateTimeFormatter));
            return dto;
        }).collect(Collectors.toList());
        return list;
    }

    public PageDTO<InformationListDTO> pageByInformationType(Long typeId, int pageNum, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);

        Page<Information> page = informationRepository.pageByTypeId(typeId, pageRequest);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        List<InformationListDTO> collect = page.get().map(item -> {
            InformationListDTO dto = new InformationListDTO()
                    .setId(item.getId())
                    .setTitle(item.getTitle())
                    .setTypeName(item.getType().getName())
                    .setSticky(item.getSticky())
                    .setCover(item.getCover())
                    .setTime(item.getUpdateTime().format(dateTimeFormatter));
            return dto;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(collect, page);
    }

    public List<InformationTypeDTO> listInformationTypes() {
        List<InformationType> all = informationTypeRepository.findAll();
        List<InformationTypeDTO> collect = all.stream().map(item -> {
            InformationTypeDTO dto = new InformationTypeDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            return dto;
        }).collect(Collectors.toList());
        return collect;
    }

    public CursorPageDTO<InformationCommentDto> pageInformationComment(Long cursor, Long infoId, Long empid) {
        LocalDateTime localDateTime;
        //把cursor毫秒值转localDateTime
        if (cursor == null || cursor == 0) {
            localDateTime = LocalDateTime.now();
        } else {
            localDateTime = new Date(cursor).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        }
        Specification<InformationComment> spec = SpecificationBuilder.builder()
                .where(Criterion.lt("createTime", localDateTime))
                .where(Criterion.eq("information.id", infoId))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<InformationComment> page = this.informationCommentRepository.findAll(spec, pageRequest);

        Map<Long, FeignEmployeeDTO> mapEmp = feignEmployeeService.findByIds(page.get().map(InformationComment::getEmployeeId).collect(Collectors.toSet()));

        List<InformationCommentDto> informationDtos = page.get().map(item -> {
            InformationCommentDto dto = new InformationCommentDto();
            if (item.getEmployeeId() != null) {
                FeignEmployeeDTO feignEmployeeDTO = mapEmp.get(item.getEmployeeId());
                if (feignEmployeeDTO != null) {
                    dto.setName(item.getAnonymous() ? null : feignEmployeeDTO.getName())
                            .setHeadImage(feignEmployeeDTO.getAvatar());
                }
            }

            dto.setAnonymous(item.getAnonymous())
                    .setContent(item.getContent())
                    .setPraise(item.getPraise())
                    .setId(item.getId())
                    .setCreateTime(item.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                    .setIsUser(item.getEmployeeId() != null && Objects.equals(SecurityUtils.getUserId(), item.getEmployeeId()))
                    .setIsPraise(item.getPraiseEmp() != null ? Arrays.asList(item.getPraiseEmp().split(",")).contains(empid.toString()) : Boolean.FALSE);
            return dto;
        }).collect(Collectors.toList());
        cursor = informationDtos.size() != 0 ? informationDtos.get(informationDtos.size() - 1).getCreateTime() : -1;
        return PageUtil.toCursorPageDTO(informationDtos, cursor);
    }

    @Transactional(rollbackFor = Exception.class)
    public void praiseInfoComment(Long infoCommentId) {
        Long empid = SecurityUtils.getUserId();
        InformationComment comment = this.informationCommentRepository.findById(infoCommentId).orElse(null);
        if (comment == null) {
            throw new BizException("", "评论信息不存在");
        }
        if (comment.getPraiseEmp() != null && Arrays.asList(comment.getPraiseEmp().split(",")).contains(empid.toString())) {
            throw new BizException("", "已赞当前评论");
        }
        this.informationCommentRepository.addPraise(infoCommentId, empid);
    }

    /**
     * 取消咨询评论赞
     *
     * @param infoCommentId
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelPraiseInfo(Long infoCommentId) {
        Long empid = SecurityUtils.getUserId();
        InformationComment comment = this.informationCommentRepository.findById(infoCommentId).orElse(null);
        if (comment == null) {
            throw new BizException("", "咨询评论不存在");
        }
        if (comment.getPraiseEmp() == null || !Arrays.asList(comment.getPraiseEmp().split(",")).contains(empid.toString())) {
            throw new BizException("", "未赞当前咨询评论无法取消赞");
        }
        comment.setPraise(comment.getPraise() - 1);
        comment.setPraiseEmp(comment.getPraiseEmp().replace("," + empid.toString(), ""));
        if (StringUtils.isBlank(comment.getPraiseEmp())) {
            comment.setPraiseEmp(null);
        }
        this.informationCommentRepository.save(comment);
    }

    /**
     * 取消咨询赞
     *
     * @param infoId
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelAddPraiseInfo(Long infoId) {
        Long empid = SecurityUtils.getUserId();
        Information information = this.informationRepository.findById(infoId).orElse(null);
        if (information == null) {
            throw new BizException("", "咨询不存在");
        }
        if (information.getPraiseEmpId() == null || !Arrays.asList(information.getPraiseEmpId().split(",")).contains(empid.toString())) {
            throw new BizException("", "未赞当前咨询无法取消");
        }
        information.setPraiseCount(information.getPraiseCount() - 1);
        information.setPraiseEmpId(information.getPraiseEmpId().replace("," + empid.toString(), ""));
        if (StringUtils.isBlank(information.getPraiseEmpId())) {
            information.setPraiseEmpId(null);
        }
        this.informationRepository.save(information);
    }

    @Transactional(rollbackFor = Exception.class)
    public void praiseInfo(Long infoId) {
        Long empid = SecurityUtils.getUserId();
        Information information = this.informationRepository.findById(infoId).orElse(null);
        if (information == null) {
            throw new BizException("", "咨询信息不存在");
        }
        if (information.getPraiseEmpId() != null && Arrays.asList(information.getPraiseEmpId().split(",")).contains(empid.toString())) {
            throw new BizException("", "已赞当前咨询");
        }
        this.informationRepository.addPraise(infoId, empid);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addInfoComment(InformationCommentAdd add) {
        InformationComment comment = new InformationComment()
                .setAnonymous(add.getAnonymous() != null ? add.getAnonymous() : Boolean.FALSE)
                .setContent(add.getContent())
                .setPraise(0)
                .setEmployeeId(SecurityUtils.getUserId())
                .setInformation(this.informationRepository.findById(add.getInfoId()).orElse(null));
        this.informationCommentRepository.save(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeInfoComment(Long id) {
        InformationComment comment = this.informationCommentRepository.findById(id).orElseThrow(() -> new BizException("", "未找到评论信息"));
        if (!Objects.equals(comment.getEmployeeId(), SecurityUtils.getUserId())) {
            throw new BizException("", "非当前用户评论，无法删除");
        }
        this.informationCommentRepository.delete(comment);
    }

    public InformationDetailDTO findById(Long infoId) {
        Long empId = SecurityUtils.getUserId();
        Information item = this.informationRepository.findById(infoId).orElse(null);
        if (item != null) {
            InformationDetailDTO dto = new InformationDetailDTO()
                    .setId(item.getId())
                    .setContent(item.getContent())
                    .setTitle(item.getTitle())
                    .setType(item.getType().getName())
                    .setPraiseCount(item.getPraiseCount())
                    .setCover(item.getCover())
                    .setIsPraise(item.getPraiseEmpId() != null ? Arrays.asList(item.getPraiseEmpId().split(",")).contains(empId.toString()) : Boolean.FALSE)
                    .setNumberOfComments(informationCommentRepository.countAllByInformationId(item.getId()).intValue())
                    .setTime(item.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")));
            return dto;
        }
        return new InformationDetailDTO();
    }
}
