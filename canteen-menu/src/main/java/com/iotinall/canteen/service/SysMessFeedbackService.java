package com.iotinall.canteen.service;


import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.feedback.FeignFeedbackDTO;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackDTO;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackEditReq;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackHandleReq;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackQueryCriteria;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignMessProductCookView;
import com.iotinall.canteen.entity.MessFeedback;
import com.iotinall.canteen.repository.MessFeedbackRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * mess_feedback ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-27 17:23:08
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysMessFeedbackService {

    @Resource
    private MessFeedbackRepository messFeedBackRepository;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    @SuppressWarnings("unchecked")
    public PageDTO<MessFeedbackDTO> pageMessFeedback(MessFeedbackQueryCriteria criteria, Pageable pageable) {
        String jpql = "from MessFeedback p ";
        StringBuilder whereSql = new StringBuilder(" where 1=1");
        Map<String, Object> params = new HashMap<>();
        if (criteria.getStatus() != null) {
            whereSql.append(" and p.status = :status");
            params.put("status", criteria.getStatus());
        }
        if (!StringUtils.isBlank(criteria.getContent())) {
            whereSql.append(" and p.content like :content");
            params.put("content", "%" + criteria.getContent() + "%");
        }
        if (criteria.getBeginDate() != null) {
            whereSql.append(" and p.createTime >= :beginDate");
            params.put("beginDate", criteria.getBeginDate());
        }
        if (criteria.getEndDate() != null) {
            whereSql.append(" and p.createTime <= :endDate");
            params.put("endDate", criteria.getEndDate().withHour(23).withMinute(59).withSecond(59));
        }
        String countJpql = "select count(*) from MessFeedback p " + whereSql.toString();
        jpql += whereSql.toString();
        Page<MessFeedback> page = messFeedBackRepository.pageQuery(jpql, countJpql, params, pageable);
        List<MessFeedbackDTO> list = page.getContent().stream().map(this::convert).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    private MessFeedbackDTO convert(MessFeedback feedBack) {
        MessFeedbackDTO messFeedBackDTO = new MessFeedbackDTO();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(feedBack, messFeedBackDTO);
        if (messFeedBackDTO.getAnonymous()) {
            messFeedBackDTO.setEmpName("匿名");
            messFeedBackDTO.setMobile("********");
            messFeedBackDTO.setRole("********");
            messFeedBackDTO.setTelephone("********");
            messFeedBackDTO.setIdNo("********");
            messFeedBackDTO.setGender(2);
            messFeedBackDTO.setOrgName("********");
        } else {
            FeignMessProductCookView feignMessProductCookView = feignEmployeeService.findCookView(feedBack.getEmployeeId());
            if (feignMessProductCookView != null) {
                messFeedBackDTO.setEmpId(feignMessProductCookView.getId());
                messFeedBackDTO.setEmpName(feignMessProductCookView.getName());
                messFeedBackDTO.setMobile(feignMessProductCookView.getMobile());
                messFeedBackDTO.setRole(feignMessProductCookView.getRole());
                messFeedBackDTO.setTelephone(feignMessProductCookView.getMobile());
                messFeedBackDTO.setIdNo(feignMessProductCookView.getIdNo());
                messFeedBackDTO.setGender(feignMessProductCookView.getGender());
                messFeedBackDTO.setOrgId(feignMessProductCookView.getOrgId());
                messFeedBackDTO.setOrgName(feignMessProductCookView.getOrgName());
            }

        }
        return messFeedBackDTO;
    }

    public MessFeedbackDTO detail(Long id) {
        Optional<MessFeedback> optional = messFeedBackRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        MessFeedback feedBack = optional.get();
        return convert(feedBack);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(MessFeedbackEditReq req) {
        Optional<MessFeedback> optional = messFeedBackRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        MessFeedback messFeedBack = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, messFeedBack);
        LocalDateTime now = LocalDateTime.now();
        messFeedBack.setUpdateTime(now);
        messFeedBackRepository.save(messFeedBack);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Optional<MessFeedback> optional = messFeedBackRepository.findById(id);
        if (!optional.isPresent()) {
            messFeedBackRepository.deleteById(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        messFeedBackRepository.deleteByIdIn(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void handle(MessFeedbackHandleReq req) {
        Optional<MessFeedback> byId = messFeedBackRepository.findById(req.getId());
        if (!byId.isPresent()) {
            throw new BizException("", "记录不存在");
        }

        MessFeedback feedBack = byId.get();
        feedBack.setHandleOpinion(req.getHandleOpinion());
        feedBack.setStatus(1);
        feedBack.setHandler(SecurityUtils.getCurrentUser().getUsername());
        feedBack.setHandleTime(LocalDateTime.now());
        try {
            FeignEmployeeDTO feignEmployeeDTO = feignEmployeeService.findById(feedBack.getEmployeeId());
            if (feignEmployeeDTO != null) {
                //TODO 发送微信消息
//                this.wxMessageSendService.sendFeedbackNotice(feignEmployeeDTO.getOpenid(), feedBack.getContent());
            }
        } catch (Exception ex) {
            log.info("发送反馈微信通知失败:{}", ex.getMessage());
        }
    }

    /**
     * 大屏-反馈列表
     *
     * @author loki
     * @date 2021/7/15 20:38
     **/
    public List<FeignFeedbackDTO> getFeedbackList() {
        Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Order.desc("createTime")));
        Page<MessFeedback> pageResult = this.messFeedBackRepository.findAll(pageable);

        List<FeignFeedbackDTO> result = new ArrayList<>();
        FeignFeedbackDTO feignFeedbackDTO;
        for (MessFeedback feedback : pageResult.getContent()) {
            feignFeedbackDTO = new FeignFeedbackDTO();
            result.add(feignFeedbackDTO);
            feignFeedbackDTO.setFeedbackTime(feedback.getCreateTime());
            feignFeedbackDTO.setContent(feedback.getContent());
            feignFeedbackDTO.setHandleOpinion(feedback.getHandleOpinion());
        }

        return result;
    }
}