package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.syspermission.SysPermissionAddReq;
import com.iotinall.canteen.dto.syspermission.SysPermissionDTO;
import com.iotinall.canteen.dto.syspermission.SysPermissionEditReq;
import com.iotinall.canteen.entity.SysPermission;
import com.iotinall.canteen.repository.SysPermissionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统权限 ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-26 14:20:57
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysPermissionService {

    @Resource
    private SysPermissionRepository sysPermissionRepository;

    @SuppressWarnings("unchecked")
    public List<SysPermission> listAll() {
        List<SysPermission> list = sysPermissionRepository.listRootPermission();
        return list.stream().filter(item -> !item.getPermission().equalsIgnoreCase("ADMIN")).collect(Collectors.toList());
    }

    public List<SysPermission> listAll(Integer type) {
        List<SysPermission> list = sysPermissionRepository.listRootPermission(type);
        return list.stream().filter(item -> !item.getPermission().equalsIgnoreCase("ADMIN")).collect(Collectors.toList());
    }

    public SysPermissionDTO detail(Long id) {
        Optional<SysPermission> optional = sysPermissionRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        SysPermissionDTO sysPermissionDTO = new SysPermissionDTO();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        // BeanUtils.copyProperties(optional.get(), sysPermissionDTO);
        return sysPermissionDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(SysPermissionAddReq req) {
        SysPermission sysPermission = new SysPermission();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, sysPermission);
        LocalDateTime now = LocalDateTime.now();
        sysPermission.setCreateTime(now);
        sysPermissionRepository.save(sysPermission);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysPermissionEditReq req) {
        Optional<SysPermission> optional = sysPermissionRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("record_not_exists", "记录不存在");
        }
        SysPermission sysPermission = optional.get();
        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, sysPermission);
        sysPermissionRepository.save(sysPermission);
    }
}