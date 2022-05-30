package com.iotinall.canteen.service;

import cn.hutool.core.collection.CollectionUtil;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.HaircutMasterAddReq;
import com.iotinall.canteen.dto.HaircutMasterDTO;
import com.iotinall.canteen.dto.HaircutMasterEditReq;
import com.iotinall.canteen.dto.HaircutMasterQueryCriteria;
import com.iotinall.canteen.entity.HaircutMaster;
import com.iotinall.canteen.entity.HaircutOrder;
import com.iotinall.canteen.entity.HaircutRoom;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.HaircutMasterRepository;
import com.iotinall.canteen.repository.HaircutOrderRepository;
import com.iotinall.canteen.repository.HaircutRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description:理发师service
 * @author: JoeLau
 * @time: 2021年06月23日 16:36:11
 */
@Service
@Slf4j
public class HaircutMasterService {
    @Resource
    private HaircutMasterRepository haircutMasterRepository;
    @Resource
    private HaircutRoomRepository haircutRoomRepository;
    @Resource
    private HaircutOrderRepository haircutOrderRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 查看理发师分页
     */
    public PageDTO<HaircutMasterDTO> page(HaircutMasterQueryCriteria criteria, Pageable pageable) {
        HaircutRoom haircutRoom = this.haircutRoomRepository.findByIdAndDeleted(criteria.getHaircutRoomId(), false);
        Specification<HaircutMaster> specification = SpecificationBuilder.builder()
                .where(Criterion.like("name", criteria.getKeywords()),
                        Criterion.eq("haircutRoom", haircutRoom),
                        Criterion.eq("deleted", false))
                .build();
        Page<HaircutMaster> page = haircutMasterRepository.findAll(specification, pageable);
        List<HaircutMasterDTO> list = page.getContent().stream().map(haircutMaster -> {
            HaircutMasterDTO haircutMasterDTO = new HaircutMasterDTO();
            BeanUtils.copyProperties(haircutMaster, haircutMasterDTO);
            //设置工作年限
            if (null != haircutMaster.getWorkingStart()) {
                int years = (int) ChronoUnit.YEARS.between(haircutMaster.getWorkingStart(), LocalDate.now());
                if(years==0){
                    years=1;
                }
                haircutMasterDTO.setWorkingYears(years);
            } else {
                haircutMasterDTO.setWorkingYears(0);
            }
            haircutMasterDTO.setHaircutRoomId(haircutMaster.getHaircutRoom().getId());
            haircutMasterDTO.setHaircutRoomName(haircutMaster.getHaircutRoom().getName());
            //等待人数
            int waitingNumber = this.haircutOrderRepository.getWaitingOrderCount(haircutMaster.getId());
            haircutMasterDTO.setWaitingNumber(waitingNumber);

            //预计等待时间
            int totalTime = 0;
            int notServedCount = this.haircutOrderRepository.getNotServedOrderCount(haircutMaster.getId());
            List<HaircutOrder> duringOrderList = this.haircutOrderRepository.findAllDuringOrder(haircutMaster.getId());
            if (CollectionUtil.isEmpty(duringOrderList)) {
                totalTime = notServedCount * 30;
            } else {
                int totalDuringTime = 0;
                for (HaircutOrder order : duringOrderList) {
                    int minutes = (int) Duration.between(order.getStartCutTime(), LocalDateTime.now()).toMinutes();
                    totalDuringTime = totalDuringTime + Math.max(30 - minutes, 0);
                }
                totalTime = notServedCount * 30 + totalDuringTime;
            }
            haircutMasterDTO.setWaitingTime(totalTime + "分钟");
            return haircutMasterDTO;
        }).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * 查看理发师列表
     *
     * @return
     */
    public List<HaircutMasterDTO> getList(Long roomId) {
        HaircutRoom haircutRoom = this.haircutRoomRepository.findByIdAndDeleted(roomId, false);
        if (null == haircutRoom) {
            throw new BizException("理发室不存在");
        }
        List<HaircutMaster> masterList = this.haircutMasterRepository.findAllByDeletedAndHaircutRoom(false, haircutRoom);
        List<HaircutMasterDTO> list = masterList.stream().map(haircutMaster -> {
            HaircutMasterDTO haircutMasterDTO = new HaircutMasterDTO();
            BeanUtils.copyProperties(haircutMaster, haircutMasterDTO);
            //设置工作年限
            if (null != haircutMaster.getWorkingStart()) {
                int years = (int) ChronoUnit.YEARS.between(haircutMaster.getWorkingStart(), LocalDate.now());
                if(years==0){
                    years=1;
                }
                haircutMasterDTO.setWorkingYears(years);
            } else {
                haircutMasterDTO.setWorkingYears(0);
            }
            haircutMasterDTO.setHaircutRoomId(haircutMaster.getHaircutRoom().getId());
            haircutMasterDTO.setHaircutRoomName(haircutMaster.getHaircutRoom().getName());
            //等待人数
            int waitingNumber = this.haircutOrderRepository.getWaitingOrderCount(haircutMaster.getId());
            haircutMasterDTO.setWaitingNumber(waitingNumber);

            //预计等待时间
            int totalTime = 0;
            int notServedCount = this.haircutOrderRepository.getNotServedOrderCount(haircutMaster.getId());
            List<HaircutOrder> duringOrderList = this.haircutOrderRepository.findAllDuringOrder(haircutMaster.getId());
            if (CollectionUtil.isEmpty(duringOrderList)) {
                totalTime = notServedCount * 30;
            } else {
                int totalDuringTime = 0;
                for (HaircutOrder order : duringOrderList) {
                    int minutes = (int) Duration.between(order.getStartCutTime(), LocalDateTime.now()).toMinutes();
                    totalDuringTime = totalDuringTime + Math.max(30 - minutes, 0);
                }
                totalTime = notServedCount * 30 + totalDuringTime;
            }
            haircutMasterDTO.setWaitingTime(totalTime + "分钟");
            return haircutMasterDTO;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 添加理发师
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(HaircutMasterAddReq addReq) {
        FeignEmployeeDTO feignEmployeeDTO = this.feignEmployeeService.findById(addReq.getEmpId());
        if (null == feignEmployeeDTO) {
            throw new BizException("该人员不存在");
        }
        HaircutMaster master = this.haircutMasterRepository.findByEmpIdAndAndDeleted(addReq.getEmpId(), false);
        if (null != master) {
            throw new BizException("该人员已绑定理发师");
        }

        HaircutRoom haircutRoom = this.haircutRoomRepository.findByIdAndDeleted(addReq.getHaircutRoomId(), false);
        if (null == haircutRoom) {
            throw new BizException("理发室不存在");
        }
        HaircutMaster haircutMaster = new HaircutMaster();
        haircutMaster.setHaircutRoom(haircutRoom);
        haircutMaster.setEmpId(addReq.getEmpId());
        //设置姓名
        if (StringUtils.isNotBlank(addReq.getName())) {
            haircutMaster.setName(addReq.getName());
        } else {
            haircutMaster.setName(feignEmployeeDTO.getName());
        }
        //设置头像
        if (StringUtils.isNotBlank(addReq.getAvatar())) {
            haircutMaster.setAvatar(addReq.getAvatar());
        } else {
            if (!StringUtils.isNotBlank(feignEmployeeDTO.getAvatar())) {
                throw new BizException("请在人员管理中上传头像");
            }
            haircutMaster.setAvatar(feignEmployeeDTO.getAvatar());
        }
        //设置手机号
        if (StringUtils.isNotBlank(addReq.getPhoneNumber())) {
            haircutMaster.setPhoneNumber(addReq.getPhoneNumber());
        } else {
            haircutMaster.setPhoneNumber(feignEmployeeDTO.getMobile());
        }
        //设置参加工作时间
        if (null != addReq.getWorkingStart()) {
            if (addReq.getWorkingStart().isBefore(LocalDate.now())) {
                haircutMaster.setWorkingStart(addReq.getWorkingStart());
            } else {
                throw new BizException("请输入正确的参加工作时间");
            }
        }
        haircutMaster.setPresentation(addReq.getPresentation());
        this.haircutMasterRepository.save(haircutMaster);
    }

    /**
     * 编辑理发师
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(HaircutMasterEditReq editReq) {
        FeignEmployeeDTO feignEmployeeDTO = this.feignEmployeeService.findById(editReq.getEmpId());
        if (null == feignEmployeeDTO) {
            throw new BizException("该人员不存在");
        }
        HaircutMaster haircutMaster = this.haircutMasterRepository.findByIdAndDeleted(editReq.getId(), false);
        if (null == haircutMaster) {
            throw new BizException("理发师不存在");
        }
        HaircutMaster master = this.haircutMasterRepository.findByEmpIdAndAndDeleted(editReq.getEmpId(), false);
        if (null != master && !master.equals(haircutMaster)) {
            throw new BizException("该人员已绑定理发师");
        }
        HaircutRoom haircutRoom = this.haircutRoomRepository.findByIdAndDeleted(editReq.getHaircutRoomId(), false);
        if (null == haircutRoom) {
            throw new BizException("理发室不存在");
        }
        haircutMaster.setHaircutRoom(haircutRoom);
        haircutMaster.setEmpId(editReq.getEmpId());
        //设置姓名
        if (StringUtils.isNotBlank(editReq.getName())) {
            haircutMaster.setName(editReq.getName());
        } else {
            haircutMaster.setName(feignEmployeeDTO.getName());
        }
        //设置头像
        if (StringUtils.isNotBlank(editReq.getAvatar())) {
            haircutMaster.setAvatar(editReq.getAvatar());
        } else {
            if (!StringUtils.isNotBlank(feignEmployeeDTO.getAvatar())) {
                throw new BizException("请在人员管理中上传头像");
            }
            haircutMaster.setAvatar(feignEmployeeDTO.getAvatar());
        }
        //设置手机号
        if (StringUtils.isNotBlank(editReq.getPhoneNumber())) {
            haircutMaster.setPhoneNumber(editReq.getPhoneNumber());
        } else {
            haircutMaster.setPhoneNumber(feignEmployeeDTO.getMobile());
        }
        //设置参加工作时间
        if (null != editReq.getWorkingStart()) {
            if (editReq.getWorkingStart().isBefore(LocalDate.now())) {
                haircutMaster.setWorkingStart(editReq.getWorkingStart());
            } else {
                throw new BizException("请输入正确的参加工作时间");
            }
        }
        haircutMaster.setPresentation(editReq.getPresentation());
        this.haircutMasterRepository.save(haircutMaster);
    }

    /**
     * 删除理发师
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            HaircutMaster haircutMaster = this.haircutMasterRepository.findByIdAndDeleted(id, false);
            if (null == haircutMaster) {
                throw new BizException("理发师不存在");
            }
            HaircutOrder haircutOrder = this.haircutOrderRepository.masterExistsUnfinishedOrder(id);
            if (ObjectUtils.isNotEmpty(haircutOrder)) {
                throw new BizException("该理发师存在未完成的理发订单");
            }
            haircutMaster.setDeleted(true);
            this.haircutMasterRepository.save(haircutMaster);
        }
    }

}
