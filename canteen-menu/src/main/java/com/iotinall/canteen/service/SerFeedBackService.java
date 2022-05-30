package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.dto.messfeedback.AppMessFeedbackDto;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackReq;
import com.iotinall.canteen.entity.MessFeedback;
import com.iotinall.canteen.repository.MessFeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 意见反馈
 *
 * @author WJH
 * @date 2019/11/612:01
 */
@Service
public class SerFeedBackService {

    @Resource
    private MessFeedbackRepository messFeedBackRepository;

    public void addFeedback(MessFeedbackReq req, Long empid) {
        MessFeedback messFeedBack = new MessFeedback()
                .setFeedType(req.getFeedType())
                .setAnonymous(req.getAnonymous() != null ? req.getAnonymous() : Boolean.FALSE)
                .setStatus(0)
                .setEmployeeId(empid)
                .setContent(req.getContent());
        this.messFeedBackRepository.save(messFeedBack);
    }

    public CursorPageDTO<AppMessFeedbackDto> listFeedback(Long cursor, Long empid) {
        Specification<MessFeedback> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("employeeId", empid))
                .where(Criterion.lt("id", cursor))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));

        Page<MessFeedback> page = this.messFeedBackRepository.findAll(spec, pageRequest);

        List<AppMessFeedbackDto> feedBackDtos = page.get().map(item -> {
            AppMessFeedbackDto backDto = new AppMessFeedbackDto()
                    .setId(item.getId())
                    .setFeedBackType(item.getFeedType())
                    .setCreateTime(item.getCreateTime() != null ? item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")) : null)
                    .setContent(item.getContent());
            return backDto;
        }).collect(Collectors.toList());

        return PageUtil.toCursorPageDTO(feedBackDtos, feedBackDtos.size() != 0 ? feedBackDtos.get(feedBackDtos.size() - 1).getId() : -1);
    }
}
