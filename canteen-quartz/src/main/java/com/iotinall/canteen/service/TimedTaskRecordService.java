package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.timedtaskrecord.TimedTaskRecordDto;
import com.iotinall.canteen.repository.TimedTaskRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TimedTaskRecordService {

    @Resource
    private TimedTaskRecordRepository timedTaskRecordRepository;

    /**
     * 定时任务执行日志
     * @param pageable
     * @return
     */
    public PageDTO<?> page(Pageable pageable) {
        Page<TimedTaskRecordDto> page = timedTaskRecordRepository.findAll(pageable).map(item -> {
            TimedTaskRecordDto recordDto = new TimedTaskRecordDto()
                    .setId(item.getId())
                    .setCreateTime(item.getCreateTime())
                    .setEndTime(item.getEndTime())
                    .setTimedTaskName(item.getTimedTask() != null ? item.getTimedTask().getName() : null)
                    .setErrorLog(item.getErrorLog())
                    .setSuccess(item.getSuccess());
            return recordDto;
        });
        return PageUtil.toPageDTO(page);
    }

}
