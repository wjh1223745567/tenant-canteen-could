package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.constants.SendMessageEnum;
import com.iotinall.canteen.dto.sendmessage.SendMessageAddReq;
import com.iotinall.canteen.dto.sendmessage.SendMessageDto;
import com.iotinall.canteen.dto.sendmessage.SendMessageEditReq;
import com.iotinall.canteen.entity.WxMessageContentConfig;
import com.iotinall.canteen.repository.WxMessageContenttConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WxMessageContentConfigService {
    @Resource
    private WxMessageContenttConfigRepository wxMessageContenttConfigRepository;

    public List<SendMessageDto> list() {
        List<SendMessageDto> sendMessageDtos = this.wxMessageContenttConfigRepository.findAll().stream().map(item -> {
            SendMessageDto sendMessageDto = new SendMessageDto();
            sendMessageDto.setId(item.getId());
            sendMessageDto.setContent(item.getContent());
            sendMessageDto.setTypeName(
                    sendMessageDto.new Type().setId(item.getType())
                            .setName(SendMessageEnum.byCode(item.getType()).getDesc())
            );
            sendMessageDto.setOpen(item.getOpen());
            if (item.getRemark() == null) {
                sendMessageDto.setRemark("暂无备注");
            } else {
                sendMessageDto.setRemark(item.getRemark());
            }
            return sendMessageDto;
        }).collect(Collectors.toList());
        return sendMessageDtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(SendMessageAddReq req) {
        WxMessageContentConfig wxMessageContentConfig = new WxMessageContentConfig();
        WxMessageContentConfig contentConfig = this.wxMessageContenttConfigRepository.findByType(req.getTypeId());
        if(contentConfig != null){
            throw new BizException("该配置类型已存在");
        }
        wxMessageContentConfig.setContent(req.getContent());
        wxMessageContentConfig.setType(req.getTypeId());
        wxMessageContentConfig.setOpen(req.getOpen());
        wxMessageContentConfig.setRemark(req.getRemark());
        this.wxMessageContenttConfigRepository.save(wxMessageContentConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(SendMessageEditReq req) {
        WxMessageContentConfig wxMessageContentConfig = this.wxMessageContenttConfigRepository.findById(req.getId()).orElseThrow(() -> new BizException("未找到该推送配置"));
        if(req.getTypeId() != wxMessageContentConfig.getType()){
            WxMessageContentConfig contentConfig = this.wxMessageContenttConfigRepository.findByType(req.getTypeId());
            if(null != contentConfig){
                throw new BizException("该配置类型已存在");
            }
        }
        wxMessageContentConfig.setContent(req.getContent());
        wxMessageContentConfig.setType(req.getTypeId());
        wxMessageContentConfig.setOpen(req.getOpen());
        wxMessageContentConfig.setRemark(req.getRemark());
        this.wxMessageContenttConfigRepository.save(wxMessageContentConfig);
    }

    public Object del(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("请选择要删除的项");
        }

        List<WxMessageContentConfig> messageContentConfigs = new ArrayList<>();
        WxMessageContentConfig wxMessageContentConfig;
        for (Long id : ids) {
            wxMessageContentConfig = this.wxMessageContenttConfigRepository.findById(id).orElseThrow(() -> new BizException("未找到该配置"));
            messageContentConfigs.add(wxMessageContentConfig);
            wxMessageContenttConfigRepository.delete(wxMessageContentConfig);
        }
        return messageContentConfigs;
    }
}
