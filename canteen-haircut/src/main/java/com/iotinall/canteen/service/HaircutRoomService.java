package com.iotinall.canteen.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.common.utils.ConvertUtils;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.HaircutRoomAddReq;
import com.iotinall.canteen.dto.HaircutRoomAppDTO;
import com.iotinall.canteen.dto.HaircutRoomDTO;
import com.iotinall.canteen.dto.HaircutRoomEditReq;
import com.iotinall.canteen.dto.HaircutRoomQueryReq;
import com.iotinall.canteen.entity.HaircutMaster;
import com.iotinall.canteen.entity.HaircutOrder;
import com.iotinall.canteen.entity.HaircutRoom;
import com.iotinall.canteen.repository.HaircutMasterRepository;
import com.iotinall.canteen.repository.HaircutOrderRepository;
import com.iotinall.canteen.repository.HaircutRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.jcajce.provider.qtesla.SignatureSpi;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:理发室service
 * @author: JoeLau
 * @time: 2021年06月23日 16:09:08
 */
@Service
@Slf4j
public class HaircutRoomService {
    @Resource
    private HaircutRoomRepository haircutRoomRepository;
    @Resource
    private HaircutMasterRepository haircutMasterRepository;
    @Resource
    private HaircutOrderRepository haircutOrderRepository;

    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 查看理发店分页
     */
    public PageDTO<HaircutRoomDTO> page(HaircutRoomQueryReq req, Pageable pageable) {
        Specification<HaircutRoom> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("deleted", false),
                        Criterion.like("name", req.getKeywords()))
                .build();
        Page<HaircutRoom> page = haircutRoomRepository.findAll(specification, pageable);
        List<HaircutRoomDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * 查看理发店列表
     *
     * @return
     */
    public List<HaircutRoomDTO> getList() {
        List<HaircutRoom> roomList = haircutRoomRepository.findAllByDeleted(false);
        List<HaircutRoomDTO> list = roomList.stream().map(this::transform).collect(Collectors.toList());
        return list;
    }

    /**
     * 实体转DTO
     */
    private HaircutRoomDTO transform(HaircutRoom haircutRoom) {
        HaircutRoomDTO haircutRoomDTO = new HaircutRoomDTO();
        BeanUtils.copyProperties(haircutRoom, haircutRoomDTO);
        return haircutRoomDTO;
    }

    /**
     * 小程序查找所有理发店
     */
    @Transactional(rollbackFor = Exception.class)
    public List<HaircutRoomAppDTO> findAll(HaircutRoomQueryReq req) {
        List<HaircutRoom> list = this.haircutRoomRepository.findAllByDeleted(false);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        List<HaircutRoomAppDTO> resultList = new ArrayList<>();
        HaircutRoomAppDTO haircutRoomAppDTO;

        //该顾客上一次完成的订单
        HaircutOrder recentOrder = this.haircutOrderRepository.getRecentOrder(SecurityUtils.getUserId());
        for (HaircutRoom room : list) {
            haircutRoomAppDTO = new HaircutRoomAppDTO();
            BeanUtils.copyProperties(room, haircutRoomAppDTO);
            if (null != req.getNowLongitude() && null != req.getNowLatitude()) {
                BigDecimal resultDistance = BigDecimal.valueOf(getDistance(req.getNowLongitude().doubleValue(), req.getNowLatitude().doubleValue(),
                        room.getLongitude().doubleValue(), room.getLatitude().doubleValue()));
                haircutRoomAppDTO.setDistance(resultDistance);
                if (resultDistance.doubleValue() >= 1) {
                    haircutRoomAppDTO.setStringDistance(resultDistance.setScale(2, BigDecimal.ROUND_HALF_UP) + "公里");
                } else if (0 <= resultDistance.doubleValue() && resultDistance.doubleValue() < 1) {
                    BigDecimal transfer = new BigDecimal(1000);
                    haircutRoomAppDTO.setStringDistance(resultDistance.multiply(transfer).setScale(2, BigDecimal.ROUND_HALF_UP) + "米");
                } else {
                    haircutRoomAppDTO.setStringDistance("--");
                }
            }
            //正在剪发的人数
            haircutRoomAppDTO.setHaircuttingNumber(this.haircutOrderRepository.getHaircuttingNumber(room.getId()));

            //是否为上次光顾
            haircutRoomAppDTO.setVisited(null != recentOrder && recentOrder.getHaircutRoom().equals(room));
            resultList.add(haircutRoomAppDTO);
        }

        if (null != req.getNowLongitude() && null != req.getNowLatitude()) {
            //比较理发店的距离，从小到大排
            Collections.sort(resultList, new Comparator<HaircutRoomAppDTO>() {
                @Override
                public int compare(HaircutRoomAppDTO o1, HaircutRoomAppDTO o2) {
                    return o1.getDistance().compareTo(o2.getDistance());
                }
            });
        }
        return resultList;
    }

    /**
     * 根据经纬度，计算两点的距离
     */
    private double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s = s * EARTH_RADIUS;
        return s;
    }


    /**
     * 添加理发室
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(HaircutRoomAddReq addReq) {
//        HaircutRoom room = this.haircutRoomRepository.findByLongitudeAndLatitudeAndDeleted(addReq.getLongitude(), addReq.getLatitude(), false);
//        if (null != room) {
//            throw new BizException("已存在地址相同的理发室");
//        }
        HaircutRoom haircutRoom = new HaircutRoom();
        BeanUtils.copyProperties(addReq, haircutRoom);
        this.haircutRoomRepository.save(haircutRoom);
    }

    /**
     * 编辑理发室
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(HaircutRoomEditReq editReq) {
        HaircutRoom haircutRoom = this.haircutRoomRepository.findByIdAndDeleted(editReq.getId(), false);
        if (null == haircutRoom) {
            throw new BizException("理发室不存在");
        }
//        HaircutRoom room = this.haircutRoomRepository.findByLongitudeAndLatitudeAndDeleted(editReq.getLongitude(), editReq.getLatitude(), false);
//        if (null != room && !room.equals(haircutRoom)) {
//            throw new BizException("已存在地址相同的理发室");
//        }
        BeanUtils.copyProperties(editReq, haircutRoom);
        this.haircutRoomRepository.save(haircutRoom);
    }

    /**
     * 删除理发室
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            HaircutRoom haircutRoom = this.haircutRoomRepository.findByIdAndDeleted(id, false);
            if (null == haircutRoom) {
                throw new BizException("理发室不存在");
            }
            HaircutOrder haircutOrder = this.haircutOrderRepository.roomExistsUnfinishedOrder(id);
            if (ObjectUtils.isNotEmpty(haircutOrder)) {
                throw new BizException("该理发室存在未完成的理发订单");
            }
            haircutRoom.setDeleted(true);
            List<HaircutMaster> list = this.haircutMasterRepository.findAllByHaircutRoom(haircutRoom);
            if (CollectionUtil.isNotEmpty(list)) {
                for (HaircutMaster haircutMaster : list) {
                    haircutMaster.setDeleted(true);
                    this.haircutMasterRepository.save(haircutMaster);
                }
            }
            this.haircutRoomRepository.save(haircutRoom);
        }
    }
//
//    /**
//     * 批量删除理发室
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public void batchDelete(String idString) {
//        String[] ids = StringUtils.split(idString, ",");
//        for (String id : ids) {
//            this.delete(Long.getLong(id));
//        }
//    }
}
