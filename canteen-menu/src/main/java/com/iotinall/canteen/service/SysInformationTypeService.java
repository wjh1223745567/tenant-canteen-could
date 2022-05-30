package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.dto.information.InformationTypeDTO;
import com.iotinall.canteen.dto.information.InformationTypeReq;
import com.iotinall.canteen.entity.InformationType;
import com.iotinall.canteen.repository.InformationTypeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资讯类型服务类
 *
 * @author WJH
 * @date 2019/11/110:19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysInformationTypeService {

    @Resource
    private InformationTypeRepository typeRepository;

    @Resource
    private FeignOrgService feignOrgService;

    public List<InformationTypeDTO> findAll() {
        List<InformationType> type = this.typeRepository.findAll();
        List<InformationTypeDTO> result = type.stream().map(item -> {
            InformationTypeDTO typeDTO = new InformationTypeDTO()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setInfoCount(item.getInfoCount())
                    .setRemark(item.getRemark())
                    .setStatus(item.getStatus())
                    .setCreateTime(item.getCreateTime())
                    .setBindOrgList(item.getBindOrg());
            return typeDTO;
        }).collect(Collectors.toList());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object save(InformationTypeReq req) {
        InformationType type;
        if (req.getId() == null) {
            type = new InformationType();
            type.setInfoCount(0);
        } else {
            type = this.typeRepository.findById(req.getId()).orElse(null);
            if (type == null) {
                throw new BizException("", "要修改的记录不存在");
            }
        }

        //获取资讯发布范围的子集
        Set<Long> orgIds = new HashSet<>();
        if (StringUtils.isNotBlank(req.getBindOrgList())) {
            List<Long> bindOrgIds = Arrays.stream(req.getBindOrgList().split(",")).map(Long::valueOf).collect(Collectors.toList());
            if(!bindOrgIds.isEmpty()){
                orgIds = this.feignOrgService.getAllChildOrg(bindOrgIds);
            }
            orgIds.addAll(bindOrgIds);
        }

        type.setRemark(req.getRemark());
        type.setName(req.getName())
                .setStatus(req.getStatus())
                .setBindOrg(req.getBindOrgList())
                .setReceiveOrg(orgIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        return this.typeRepository.saveAndFlush(type);
    }

    public Object deleted(Long id) {
        //判断该类型是否已经被使用
        InformationType type = this.typeRepository.findById(id).orElseThrow(() -> new BizException("", ""));
        
        if (null == type) {
            throw new BizException("", "类型不存在");
        }
        if (type.getInfoCount() > 0) {
            throw new BizException("", "已存在该类型的资讯，不能删除");
        }
        this.typeRepository.deleteById(id);

        return type;
    }
}
