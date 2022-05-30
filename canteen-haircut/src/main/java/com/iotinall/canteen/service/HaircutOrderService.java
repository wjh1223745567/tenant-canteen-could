package com.iotinall.canteen.service;

import cn.hutool.core.collection.CollectionUtil;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constants.HaircutOrderStatusEnum;
import com.iotinall.canteen.dto.HaircutOrderAddReq;
import com.iotinall.canteen.dto.HaircutOrderDTO;
import com.iotinall.canteen.dto.HaircutOrderEditReq;
import com.iotinall.canteen.dto.HaircutOrderQueryReq;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.entity.HaircutMaster;
import com.iotinall.canteen.entity.HaircutOrder;
import com.iotinall.canteen.entity.HaircutRoom;
import com.iotinall.canteen.repository.HaircutMasterRepository;
import com.iotinall.canteen.repository.HaircutOrderRepository;
import com.iotinall.canteen.repository.HaircutRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:理发订单service
 * @author: JoeLau
 * @time: 2021年06月23日 16:38:13
 */

@Slf4j
@Service
public class HaircutOrderService {
    @Resource
    private HaircutOrderRepository haircutOrderRepository;
    @Resource
    private HaircutRoomRepository haircutRoomRepository;
    @Resource
    private HaircutMasterRepository haircutMasterRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 查看理发订单分页
     */
    public PageDTO<HaircutOrderDTO> page(HaircutOrderQueryReq req, Pageable pageable) {
        LocalDateTime begin = null, end = null;
        HaircutRoom haircutRoom = this.haircutRoomRepository.findByIdAndDeleted(req.getRoomId(), false);
        HaircutMaster haircutMaster = this.haircutMasterRepository.findByIdAndDeleted(req.getMasterId(), false);
        if (StringUtils.isNotBlank(req.getStartTime()) && StringUtils.isNotBlank(req.getEndTime())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            begin = LocalDateTime.parse(req.getStartTime() + " 00:00:00", df);
            end = LocalDateTime.parse(req.getEndTime() + " 23:59:59", df);
        }

        Long empId = null;
        if (!req.getExhaustive()) {
            empId = SecurityUtils.getUserId();
        }

        List<Integer> statusList = null;
        if (null != req.getStatus()) {
            statusList = new ArrayList<>();
            if (HaircutOrderStatusEnum.NOT_SERVED.getCode() == req.getStatus() || HaircutOrderStatusEnum.IS_DURING.getCode() == req.getStatus()) {
                statusList.add(HaircutOrderStatusEnum.NOT_SERVED.getCode());
                statusList.add(HaircutOrderStatusEnum.IS_DURING.getCode());
            } else {
                statusList.add(req.getStatus());
            }
        }

        Specification spec = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("haircutMaster.name", req.getKeywords()),
                        Criterion.like("haircutRoom.name", req.getKeywords()),
                        Criterion.like("empName", req.getKeywords()))
                .where(Criterion.in("status", statusList),
                        Criterion.eq("haircutRoom", haircutRoom),
                        Criterion.eq("haircutMaster", haircutMaster),
                        Criterion.gte("pickTime", begin),
                        Criterion.lte("pickTime", end),
                        Criterion.eq("empId", empId)
                )
                .build();
        Page<HaircutOrder> page = haircutOrderRepository.findAll(spec, pageable);
        List<HaircutOrderDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 小程序理发师订单查询
     * @Date 2021/7/9  10:24
     * @Param
     */
    public PageDTO<HaircutOrderDTO> masterPage(Boolean status, Pageable pageable) {
        Long empId = SecurityUtils.getUserId();
        HaircutMaster haircutMaster = this.haircutMasterRepository.findByEmpIdAndAndDeleted(empId, false);
        if (null == haircutMaster) {
            throw new BizException("您不是理发师");
        }
        List<Integer> statusList = new ArrayList<>();
        if (status) {
            statusList.add(HaircutOrderStatusEnum.IS_FINISHED.getCode());//已完成
            statusList.add(HaircutOrderStatusEnum.IS_PASSED.getCode());//已过号
        } else {
            statusList.add(HaircutOrderStatusEnum.NOT_SERVED.getCode());//待服务
            statusList.add(HaircutOrderStatusEnum.IS_DURING.getCode());//服务中
        }
        Specification<HaircutOrder> specification = SpecificationBuilder.builder()
                .where(Criterion.in("status", statusList),
                        Criterion.eq("haircutMaster", haircutMaster))
                .build();
        Page<HaircutOrder> page = haircutOrderRepository.findAll(specification, pageable);
        List<HaircutOrderDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * 实体转DTO
     */
    public HaircutOrderDTO transform(HaircutOrder haircutOrder) {
        HaircutMaster master = haircutOrder.getHaircutMaster();
        HaircutOrderDTO haircutOrderDTO = new HaircutOrderDTO();
        BeanUtils.copyProperties(haircutOrder, haircutOrderDTO);
        haircutOrderDTO.setHaircutRoomName(haircutOrder.getHaircutRoom().getName());
        haircutOrderDTO.setHaircutMasterName(haircutOrder.getHaircutMaster().getName());
        //订单为待服务时要计算等待时间
        if (haircutOrder.getStatus() == HaircutOrderStatusEnum.NOT_SERVED.getCode()) {
            //该订单前的待服务-0订单数量
            int waitingNumber = this.haircutOrderRepository.getEmpNotServedOrderCount(master.getId(), haircutOrder.getPickTime());
            //该理发师服务中-2订单数量
            int duringNumber = this.haircutOrderRepository.getDuringOrderCount(master.getId());
            haircutOrderDTO.setWaitingNumber(waitingNumber+duringNumber);

            //预计等待时间
            int totalTime;
            List<HaircutOrder> duringOrderList = this.haircutOrderRepository.findAllDuringOrder(master.getId());
            if (CollectionUtil.isEmpty(duringOrderList)) {
                totalTime = waitingNumber * 30;
            } else {
                int totalDuringTime = 0;
                for (HaircutOrder order : duringOrderList) {
                    int minutes = (int) Duration.between(order.getStartCutTime(), LocalDateTime.now()).toMinutes();
                    totalDuringTime = totalDuringTime + Math.max(30 - minutes, 0);
                }
                totalTime = waitingNumber * 30 + totalDuringTime;
            }
            haircutOrderDTO.setWaitingTime(totalTime + "分钟");
        }
        return haircutOrderDTO;
    }

    /**
     * 添加理发订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(HaircutOrderAddReq req) {
        HaircutRoom haircutRoom = this.haircutRoomRepository.findById(req.getHaircutRoomId()).orElseThrow(() -> new BizException("理发室不存在"));
        HaircutMaster haircutMaster = this.haircutMasterRepository.findById(req.getHaircutMasterId()).orElseThrow(() -> new BizException("理发师不存在"));
        if (!haircutMaster.getHaircutRoom().equals(haircutRoom)) {
            throw new BizException("该理发师不属于该理发室");
        }
        boolean haveNotServedOrder01 = this.haircutOrderRepository.existsByEmpIdAndStatus(SecurityUtils.getUserId(), HaircutOrderStatusEnum.NOT_SERVED.getCode());
        boolean haveNotServedOrder02 = this.haircutOrderRepository.existsByEmpIdAndStatus(SecurityUtils.getUserId(), HaircutOrderStatusEnum.IS_DURING.getCode());
        if (haveNotServedOrder01 || haveNotServedOrder02) {
            throw new BizException("已经有订单未完成，可选择先取消订单");
        }
        FeignEmployeeDTO feignEmployeeDTO = this.feignEmployeeService.findById(SecurityUtils.getUserId());
        if (null == feignEmployeeDTO) {
            throw new BizException("该人员不存在");
        }

        HaircutOrder haircutOrder = new HaircutOrder();
        haircutOrder.setHaircutRoom(haircutRoom);
        haircutOrder.setHaircutMaster(haircutMaster);
        haircutOrder.setEmpId(SecurityUtils.getUserId());
        haircutOrder.setEmpName(feignEmployeeDTO.getName());
        haircutOrder.setEmpPhone(feignEmployeeDTO.getMobile());
        haircutOrder.setPickTime(LocalDateTime.now());
        haircutOrder.setStatus(HaircutOrderStatusEnum.NOT_SERVED.getCode());
        //订单预约号码
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String randomString = RandomStringUtils.randomNumeric(8);
        haircutOrder.setOrderNumber(time + randomString);

        this.haircutOrderRepository.save(haircutOrder);
    }

    /**
     * 编辑理发订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(HaircutOrderEditReq req) {
        HaircutOrder haircutOrder = this.haircutOrderRepository.findById(req.getId()).orElseThrow(() -> new BizException("该订单不存在"));
        LocalDateTime now = LocalDateTime.now();
        switch (req.getStatus()) {
            case 1:
                if (haircutOrder.getStatus() == HaircutOrderStatusEnum.NOT_SERVED.getCode()) {
                    haircutOrder.setStatus(HaircutOrderStatusEnum.IS_CANCEL.getCode());
                    haircutOrder.setCancelTime(now);
                } else {
                    throw new BizException("该理发订单不是待服务状态，不能被取消");
                }
                break;
            case 2:
                HaircutMaster haircutMaster = this.haircutMasterRepository.findByEmpIdAndAndDeleted(SecurityUtils.getUserId(),false);
                if (null == haircutMaster) {
                    throw new BizException("您不是理发师");
                }
                List<HaircutOrder> duringOrderList = this.haircutOrderRepository.findAllDuringOrder(haircutMaster.getId());
                if(!CollectionUtil.isEmpty(duringOrderList)){
                    throw new BizException("您有未服务完的订单");
                }
                if (haircutOrder.getStatus() == HaircutOrderStatusEnum.NOT_SERVED.getCode()) {
                    haircutOrder.setStatus(HaircutOrderStatusEnum.IS_DURING.getCode());
                    haircutOrder.setStartCutTime(now);
                } else {
                    throw new BizException("该理发订单不是待服务状态，不能开始理发");
                }
                break;
            case 3:
                if (haircutOrder.getStatus() == HaircutOrderStatusEnum.IS_DURING.getCode()) {
                    haircutOrder.setStatus(HaircutOrderStatusEnum.IS_FINISHED.getCode());
                    haircutOrder.setFinishedTime(now);
                } else {
                    throw new BizException("该理发订单不是服务中状态，不能进行完成操作");
                }
                break;
            case 4:
                long minutes = Duration.between(haircutOrder.getPickTime(), now).toMinutes();
                if (haircutOrder.getStatus() == HaircutOrderStatusEnum.NOT_SERVED.getCode() && minutes > 10) {
                    haircutOrder.setStatus(HaircutOrderStatusEnum.IS_PASSED.getCode());
                    haircutOrder.setPassedTime(now);
                } else {
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String passTime = haircutOrder.getPickTime().plusMinutes(10).format(fmt);
                    throw new BizException("订单在预约后十分钟才能过号，即 "+passTime+" 后才能进行过号操作");
                }
                break;
            default:
                throw new BizException("传入状态参数错误");
        }
        this.haircutOrderRepository.save(haircutOrder);
    }

}
