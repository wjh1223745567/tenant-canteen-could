package com.iotinall.canteen.service;

import com.iotinall.canteen.common.constant.OperationLogType;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.repository.OperationLogRepository;
import com.iotinall.canteen.dto.operationlog.OperationLogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class OperationLogService {

    @Resource
    private OperationLogRepository operationLogRepository;

    /**
     * 分页
     *
     * @param pageable
     * @return
     */
    public PageDTO<OperationLogDto> page(Pageable pageable) {
        Page<OperationLogDto> page = this.operationLogRepository.findAll(pageable).map(item ->
                new OperationLogDto()
                        .setClassName(item.getClassName())
                        .setData(item.getData())
                        .setType(item.getType())
                        .setCreateTime(item.getCreateTime())
                        .setTypeName(OperationLogType.findByCode(item.getType()).getName())
        );
        return PageUtil.toPageDTO(page);
    }

}
